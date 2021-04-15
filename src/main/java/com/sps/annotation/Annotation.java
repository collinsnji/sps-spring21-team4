package com.sps.annotation;

/**
 * Class that stores information about an annotation
 */
public class Annotation {
  private final long poemId;
  private final long annotationId;
  private final String lineId;
  private final String annotationText;
  private final String addedBy;
  private final long dateAdded;

  /**
   * Creates a new annotation
   *
   * @param poemId         - The poem's unique id
   * @param annotationId   - The unique id for the annotation
   * @param lineId         - Unique line id
   * @param annotationText - The annotation text
   * @param addedBy        - Name of user who added the annotation
   * @param dateAdded      - Timestamp indicated when the annotation was added
   */
  public Annotation(final long poemId, final long annotationId, final String lineId,
      final String annotationText, final String addedBy, final long dateAdded) {
    this.poemId = poemId;
    this.annotationId = annotationId;
    this.lineId = lineId;
    this.annotationText = annotationText;
    this.addedBy = addedBy;
    this.dateAdded = dateAdded;
  }
}