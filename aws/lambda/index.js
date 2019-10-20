const AWS = require('aws-sdk');
const UUID = require('uuid/v1');
const EXIF = require('exifreader')

//*/ get reference to S3 client 
var s3 = new AWS.S3();
var dynamodb = new AWS.DynamoDB({apiVersion: '2012-08-10'});

exports.handler = (event, context, callback) => {
     let encodedImageString = JSON.parse(event.body).image;
     let image_id = UUID();
     var filePath = "images/" + image_id + ".jpg";
     var image_buffer = new Buffer(encodedImageString, 'base64')
     var params = {
       "Body": image_buffer,
       "Bucket": "fire-app-data",
       "Key": filePath,
       "ContentEncoding": 'base64',
       "ContentType": 'image/jpg'
    };

    s3.upload(params, function(err, data){
       if(err) {
           callback(err, null);
       } else {
           let response = {
            "statusCode": 200,
            "headers": {
            "my_header": "my_value"
        },
        "body": JSON.stringify(data),
    };
           callback(null, response);
    }
    });

    var image_metadata = EXIF.load(image_buffer);

    console.log(image_metadata);
    

    var params = {
        Item: {
         "id": { S: image_id},
         "latitude": { S: ""+image_metadata.GPSLatitude.description}, 
         "longitude": { S: ""+image_metadata.GPSLatitude.description * -1},
         "s3Path": {S: "https://fire-app-data.s3-us-west-2.amazonaws.com/images/"+ image_id +".jpg"},
         "timestamp": {S: image_metadata.DateTimeOriginal.description}
        }, 
        ReturnConsumedCapacity: "TOTAL", 
        TableName: "fire_images"
       };
       dynamodb.putItem(params, function(err, data) {
         if (err) console.log(err, err.stack); // an error occurred
         else     console.log(data);           // successful response
       });


};