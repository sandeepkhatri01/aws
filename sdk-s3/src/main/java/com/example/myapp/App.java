package com.example.myapp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.S3Client;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static final String AWS_ACCESS_KEY="AWS_ACCESS_KEY_ID";
	private static final String AWS_SECRET_KEY="AWS_SECRET_KEY_ID";


	public static void main(String[] args) throws IOException {
		String accessKey = System.getenv(AWS_ACCESS_KEY);
		String secretKey = System.getenv(AWS_SECRET_KEY);

		AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(accessKey, secretKey, "");


		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(sessionCredentials)).region(region).build();

		String bucket = "bucket" + System.currentTimeMillis();
		String key = "key";

		tutorialSetup(s3, bucket, region);

		System.out.println("Uploading object...");

		s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
				RequestBody.fromString("Testing with the {sdk-java}"));

		System.out.println("Upload complete");
		System.out.printf("%n");

		cleanUp(s3, bucket, key);

		System.out.println("Closing the connection to {S3}");
		s3.close();
		System.out.println("Connection closed");
		System.out.println("Exiting...");
	}

	public static void tutorialSetup(S3Client s3Client, String bucketName, Region region) {
		try {
			s3Client.createBucket(
					CreateBucketRequest.builder().bucket(bucketName)
							.build());
			System.out.println("Creating bucket: " + bucketName);
			s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder().bucket(bucketName).build());
			System.out.println(bucketName + " is ready.");
			System.out.printf("%n");
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

	public static void cleanUp(S3Client s3Client, String bucketName, String keyName) {
		System.out.println("Cleaning up...");
		try {
			System.out.println("Deleting object: " + keyName);
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(keyName)
					.build();
			s3Client.deleteObject(deleteObjectRequest);
			System.out.println(keyName + " has been deleted.");
			System.out.println("Deleting bucket: " + bucketName);
			DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
			//s3Client.deleteBucket(deleteBucketRequest);
			System.out.println(bucketName + " has been deleted.");
			System.out.printf("%n");
		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
		System.out.println("Cleanup complete");
		System.out.printf("%n");
	}
}