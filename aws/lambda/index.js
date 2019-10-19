const AWS = require('aws-sdk');
const UUID = require('uuid/v1');
const EXIF = require('exifreader')

//*/ get reference to S3 client 
var s3 = new AWS.S3();
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
    console.log("ijsflnvsk")
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
    
};