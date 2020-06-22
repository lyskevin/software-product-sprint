package com.google.sps.util;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class SentimentAnalysisUtil {
  public static float getSentimentScore(String text) {
    float score = 0;

    try {
      Document document =
          Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
      LanguageServiceClient languageService = LanguageServiceClient.create();
      Sentiment sentiment = languageService.analyzeSentiment(document).getDocumentSentiment();
      languageService.close();
      score = sentiment.getScore();
    } catch (IOException ioe) {
      // An IOException is thrown if the language service client cannot be instantiated.
      // In this case, the sentiment score remains as 0.
    }

    return score;
  }
}
