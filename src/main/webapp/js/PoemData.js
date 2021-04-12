import { PoemUtils } from './PoemUtils.js';

class PoemData {
  constructor() {
    this.poemData = null;
    this.poemId = null;
    this.lineIds = null;
    this.poemContainer = document.querySelector('.poem-content');
    this.annotationContentContainer = document.querySelector('.annotations-content');
    this.addAnnotationContainer = document.querySelector('.add-annotation');
  }

  async getPoemData() {
    try {
      const response = await fetch(`/poem?id=${this.poemId}`);
      const poemDataJson = await response.json();
      this.poemData = poemDataJson[0];
      return poemDataJson;
    } catch (err) {
      throw new Error(err);
    }
  }

  /**
   * Append the poem information to the page
   */
  addPoemToPage() {
    // Set the poem id
    this.poemContainer.setAttribute('data-poemid', this.poemData.id);
    // Add poem title
    let poemTitle = PoemUtils.createElement('h3', this.poemData.poemTitle, { class: 'poem-title' });
    let poetName = PoemUtils.createElement(
      'p', `by ${this.poemData.poetName}`,
      { class: 'my-3 poet-name text-small text-bold text-uppercase' });

    let poemLines = this.constructPoemLines();
    let poemMetadata = this.constructPoemMetadata();
    this.poemContainer.appendChild(poemTitle);
    this.poemContainer.appendChild(poetName);
    this.poemContainer.appendChild(poemLines);
    this.poemContainer.appendChild(poemMetadata);
  }

  /**
   * Construct the poem lines
   * @returns {HTMLElement} - An html element with all the poem lines
   */
  constructPoemLines() {
    const poemLinesContainer = PoemUtils.createElement('div', null, null);
    const poemLines = JSON.parse(this.poemData.poemLines);
    for (let line of poemLines) {
      let lineClass = window.lineIds.includes(line.lineId) ? 'poem-line annotated' : 'poem-line';
      let lineElement = PoemUtils.createElement('p', line.lineText, {
        class: lineClass,
        'data-lineid': line.lineId
      });
      lineElement.addEventListener('click', () => { this.handleLineClick(line.lineId, lineElement) });
      poemLinesContainer.appendChild(lineElement);
    }
    return poemLinesContainer;
  }

  /**
   * Construct the poem metadata
   * @returns {HTMLElement} - An html element with the poem metadata
   */
  constructPoemMetadata() {
    let poemSource = PoemUtils.createElement(
      'div', `Source: ${this.poemData.source}`,
      { class: 'text-small text-italic text-metadata mt-2' });
    let poemDateAdded = PoemUtils.createElement(
      'span', `Added on ${new Date(this.poemData.dateAdded).toDateString()}`,
      { class: 'text-small text-italic text-metadata' });
    let metadataContainer = PoemUtils.createElement('div', null, null);
    metadataContainer.appendChild(poemSource);
    metadataContainer.appendChild(poemDateAdded);
    return metadataContainer;
  }

  /**
   * Handle the click event listener on the poem lines
   * @param {number} lineId - The poem line ID
   * @param {HTMLElement} lineElement - The html element for the poem line
   */
  handleLineClick(lineId, lineElement) {
    if (window.lineIds.includes(lineId)) {
      this.showAnnotation(lineId, lineElement);
    } else {
      this.showAnnotationForm(lineId, lineElement);
    }
  }

  /**
   * Show the annotations attached to a line
   * @param {number} lineId - The poem line ID
   * @param {HTMLElement} lineElementTarget - The html element for the poem line
   */
  showAnnotation(lineId, lineElementTarget) {
    let annotationsContentText = document.querySelector('.annotations-content-text');
    let lineAnnotations = window.annotationData.filter(annotation => annotation.lineId == lineId);
    let allAnnotationsForLine = [];
    lineAnnotations.forEach(annotation => {
      allAnnotationsForLine.push(annotation.annotationText);
    });
    // Unhide the annotations content container while hiding the new
    // annotations form
    this.addAnnotationContainer.classList.add('hidden');
    this.annotationContentContainer.classList.remove('hidden');
    annotationsContentText.innerHTML = allAnnotationsForLine.join('\n').replace(/\n/g, '<br>');

    // Update arrow position
    PoemUtils.updateArrowPosition(lineElementTarget, this.annotationContentContainer);
  }

  /**
   * Show the add annotation form
   * @param {number} lineId - The poem line ID
   * @param {HTMLElement} lineElementTarget - The html element for the poem line
   */
  showAnnotationForm(lineId, lineElementTarget) {
    this.addAnnotationContainer.classList.remove('hidden');
    this.annotationContentContainer.classList.add('hidden');
    this.addAnnotationContainer.dataset.lineid = lineId;
    this.addAnnotationContainer.dataset.poemid = this.poemData.id;

    // Update arrow position
    PoemUtils.updateArrowPosition(lineElementTarget, this.addAnnotationContainer);
  }

  async init() {
    const params = new URLSearchParams(window.location.search);
    this.poemId = params.get('id');
    if (this.poemId) {
      await this.getPoemData();
      await PoemUtils.getPoemAnnotations(this.poemId);
      this.addPoemToPage();
      PoemUtils.closeSectionHandler();
    }
  }
}

document.addEventListener('DOMContentLoaded', () => {
  new PoemData().init();
})
