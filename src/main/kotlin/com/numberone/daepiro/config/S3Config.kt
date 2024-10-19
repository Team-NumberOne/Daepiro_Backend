package com.numberone.daepiro.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    @Value("\${s3.credentials.access}")
    private val access: String,
    @Value("\${s3.credentials.secret}")
    private val secret: String,
    @Value("\${s3.region}")
    private val region: String,
) {
    @Bean
    fun s3Client(): AmazonS3Client {
        val credentials = BasicAWSCredentials(access, secret)
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build() as AmazonS3Client
    }
}
