/**
 * Add a new poem
 */
class AddPoem {
  /**
   * Parse the poem text into lines
   * @param {string} text Full text of poem
   * @returns {Array} The individual lines of the poem
   * @todo Write a function that handles stanzas and empty lines
   */
  parsePoemLines(text) {
    return text.replace('\r', '').split('\n');
  }

  /**
   * Post poem data to server
   * @param {object} data The poem data
   */
  postPoemData(data) {
    try {
      fetch('/new-poem', {
        'method': 'POST',
        'headers': { 'Content-Type': 'application/json' },
        'body': JSON.stringify(data)
      })
        .then(response => response.json())
        .then(response => {
          response = JSON.parse(response);
          if (response.code == 200) {
            // @todo - Ideally, on success, the page should load the new poem page
            alert("Added successfully");
            window.location.href = "/";
          }
          else {
            alert("Something went wrong");
            window.location.href = "/";
          }
        })
    } catch (err) {
      throw new Error(err);
    }
  }

  init() {
    let submitAnnotationButton = document.querySelector('#submit-btn');
    submitAnnotationButton.addEventListener('click', (e) => {
      let formData = new FormData(document.querySelector('#new-poem-form'));
      let fullText = formData.get('fullText');
      let poemLines = this.parsePoemLines(fullText);
      let data = Object.fromEntries(formData.entries());
      data['poemLines'] = poemLines;
      data.source = data.source || '';
      this.postPoemData(data);
      e.preventDefault();
    });
  }
}
// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
  new AddPoem().init();
});
