package com.google.sps.util;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.Optional;

public class SentimentAnalysisUtil {
  private Optional<LanguageServiceClient> languageService;

  public SentimentAnalysisUtil() {
    try {
      languageService = Optional.ofNullable(LanguageServiceClient.create());
    } catch (IOException ioe) {
      languageService = Optional.empty();
    }
  }

  public float getSentimentScore(String text) {
    float score = 0;

    if (languageService.isPresent()) {
      Document document =
          Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
      Sentiment sentiment =
          languageService.get().analyzeSentiment(document).getDocumentSentiment();
      score = sentiment.getScore();
    }

    return score;
  }
}
