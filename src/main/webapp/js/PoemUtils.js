
// Common functions used throughout the project

export const PoemUtils = {
  /**
 * Creates an html element with various properties
 * @param {string} tag The HTML tag name
 * @param {string} content The inner html content for the tag
 * @param {object} attributes A key-value pair of attributes for the element
 * @returns {HTMLElement} An html element with the specified properties
 */
  createElement: (tag, content, attributes) => {
    let element = document.createElement(tag);
    if (content) {
      element.innerHTML = content;
    }
    if (attributes) {
      for (let key in attributes) {
        element.setAttribute(key, attributes[key]);
      }
    }
    return element;
  },

  /**
   * Set the left pointing arrow on the annotations section when a line is clicked
   * 
   * @param {HTMLElement} lineElement - The html line element
   * @param {HTMLElement} offsetElement  - The html element used to calculate the offset
   */
  updateArrowPosition: (lineElement, offsetElement) => {
    // Update left arrow position
    let root = document.documentElement;
    let arrowPosition = (lineElement.getBoundingClientRect().y - offsetElement.getBoundingClientRect().y) - 7.5;
    root.style.setProperty('--arrow-y-position', `${arrowPosition}px`);
  },

  /**
   * Close a section when the close button is clicked
   */
  closeSectionHandler: () => {
    let addAnnotation = document.querySelector('.add-annotation');
    let annotationsContent = document.querySelector('.annotations-content');
    let closeButtons = document.querySelectorAll('.close-section');
    closeButtons.forEach(closeButton => {
      closeButton.addEventListener('click', () => {
        addAnnotation.classList.add('hidden');
        annotationsContent.classList.add('hidden');
      });
    });
  },

  /**
   * Get all annotations for a poem
   * @param {number} poemId - The poem ID
   * @returns {object} - Returns a json object with the annotations data
   */
  getPoemAnnotations: async (poemId) => {
    try {
      const response = await fetch(`/annotations?poemId=${poemId}`);
      const annotationsDataJson = await response.json();
      window.annotationData = annotationsDataJson;
      window.lineIds = annotationsDataJson.map(annotation => parseInt(annotation.lineId));
      return annotationsDataJson;
    } catch (err) {
      throw new Error(err);
    }
  }
}
