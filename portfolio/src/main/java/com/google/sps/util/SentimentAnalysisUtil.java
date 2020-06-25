package com.google.sps.util;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.Optional;

public class SentimentAnalysisUtil {
  private final LanguageServiceClient languageService;

  public SentimentAnalysisUtil() throws IOException {
    languageService = LanguageServiceClient.create();
  }

  public float getSentimentScore(String text) {
    Document document =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    Sentiment sentiment = languageService.analyzeSentiment(document).getDocumentSentiment();
    return sentiment.getScore();
  }
}
