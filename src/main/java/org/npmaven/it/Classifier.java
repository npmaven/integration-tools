package org.npmaven.it;

/**
 * Asset classifiers to select main vs min vs min.map, etc
 */
public enum Classifier {
    /** The MAIN asset classifier is for the non-minified, etc resource */
    MAIN,
    /** The MIN asset classifier is for the minified resource if available */
    MIN,
    /** The MAP asset classifier is for the minified map resource if available */
    MAP,
}
