package com.google.sps.util;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.Optional;

public class SentimentAnalysisUtil {
  private final Optional<LanguageServiceClient> languageService;

  public SentimentAnalysisUtil() {
    LanguageServiceClient languageServiceClient;

    try {
      languageServiceClient = LanguageServiceClient.create();
    } catch (IOException ioe) {
      languageServiceClient = null;
    }

    languageService = Optional.ofNullable(languageServiceClient);
  }

  public String getSentimentScore(String text) {
    if (languageService.isPresent()) {
      Document document =
          Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment =
          languageService.get().analyzeSentiment(document).getDocumentSentiment();
      return String.format("%.2f", sentiment.getScore());
    } else {
      return "Unavailable at this time.";
    }
  }
}
