package com.alisonjc.buzzerbutler.helpers;


import android.media.Image;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class CompareFaces {

    public static boolean verifyPhoto(byte[] photoData) {
        Float similarityThreshold = 70F;
        String sourceImage = "source.jpg";
        String targetImage = "target.jpg";
        ByteBuffer sourceImageBytes = null;
        ByteBuffer targetImageBytes = null;

//        CompareFacesRequest request = new CompareFacesRequest()
//                .withSourceImage(source)
//                .withTargetImage(target)
//                .withSimilarityThreshold(similarityThreshold);
//
//        // Call operation
//        CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);
//
//        // Display results
//        List<CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
//        for (CompareFacesMatch match : faceDetails) {
//            ComparedFace face = match.getFace();
//            BoundingBox position = face.getBoundingBox();
//            Float confidence = face.getConfidence();
//            System.out.println("Face at " + position.getLeft().toString()
//                    + " " + position.getTop()
//                    + " matches with " + confidence.toString()
//                    + "% confidence.");
//
//            if (confidence > .9) {
//                return true;
//            }
//        }
        return true;
//        return false;
    }

}

