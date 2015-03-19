package org.npmaven.it;

/**
 * Asset classifiers to select main vs min vs min.map, etc
 */
public enum Classifier {
    /** The DEFAULT asset classifier is for the non-minified, etc resource */
    DEFAULT,
    /** The MIN asset classifier is for the minified resource if available */
    MIN,
    /** The MAP asset classifier is for the source map resource if available */
    MAP,
}
