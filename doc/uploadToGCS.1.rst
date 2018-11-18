## uploadToS3
Upload a given static asset Upload to an aws S3 bucket

### Description  
```groovy
uploadToS3(String filePath, String bucketName)
```

### Parameters
  - **filePath:** the path (including file name) to file to be uploaded
  - **bucketPame:** the path (including folders) to the S3 bucket where the file will be uploaded

### Return Values
  None.

### Examples
```groovy
uploadToS3("userAgentdb.dat", "nandaparbat/static-asset")
```
The above example will push the file **userAgentdb.dat** to the S3 bucket **nandaparbat/static-asset**
