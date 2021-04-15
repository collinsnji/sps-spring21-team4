import { PoemUtils } from './PoemUtils.js';

/**
 * Add a new annotation to a line
 */
class AddAnnotation {
  /**
   * Post new annotation to server
   * @param {object} data - The annotation data
   */
  async postAnnotation(data) {
    try {
      const response = await fetch('/new-annotation', {
        'method': 'POST',
        'headers': { 'Content-Type': 'application/json' },
        'body': JSON.stringify(data)
      });
      let responseJson = await response.json();
      console.log(JSON.parse(responseJson.toString()));
      return JSON.parse(responseJson);
    } catch (err) {
      throw new Error(err);
    }
  }

  /**
   * Handle the new annotation form submission by visually updating the page after submission complete
   * @param {object} responseJson - The response object after from form submission
   * @param {number} lineId - The poem line ID
   */
  handleAnnotationSubmission(responseJson, lineId) {
    if (responseJson.code == 200) {
      document.querySelector(`[data-lineid='${lineId}']`).classList.add('annotated');
      document.querySelector('.add-annotation').classList.add('hidden');
    }
    else {
      window.location.href = window.location.origin + '/login';
    }
  }

  init() {
    let submitAnnotationButton = document.querySelector('#annotation-submit-btn');
    submitAnnotationButton.addEventListener('click', (e) => {
      e.preventDefault();
      let dataset = document.querySelector('.add-annotation').dataset;
      let formData = new FormData(document.querySelector('#new-annotation-form'));
      let data = Object.fromEntries(formData.entries());
      data['poemId'] = dataset.poemid;
      data['lineId'] = dataset.lineid;
      // Reset the textarea
      document.querySelector('.annotation-textarea').value = '';

      // Post the json data to the server
      this.postAnnotation(data).then(response => {
        this.handleAnnotationSubmission(response, dataset.lineid);
        PoemUtils.getPoemAnnotations(parseInt(dataset.poemid));
      });
    });
  }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
  new AddAnnotation().init();
});
