package com.example.swadheen.customview;

import java.util.List;
import com.example.swadheen.Classification.Classifier.Recognition;

public interface ResultsView {
    public void setResults(final List<Recognition> results);
}
