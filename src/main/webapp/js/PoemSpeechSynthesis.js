export class PoemSpeechSynthesis {
  async readPoem(fullText) {
    let data = {
      poemText: fullText
    }
    return fetch('/read-poem', {
      'method': 'POST',
      'headers': { 'Content-Type': 'application/json' },
      'body': JSON.stringify(data)
    }).then(async (response) => {
      let reader = response.body.getReader();
      const stream = await reader.read();
      return stream;
    });
  }

  async init(fullText) {
    if (fullText) {
      await this.readPoem(fullText).then(response => {
        let blob = new Blob([response.value], { type: 'audio/mp3' });
        let url = window.URL.createObjectURL(blob)
        let poemAudioElement = new Audio();
        poemAudioElement.src = url;
        poemAudioElement.id = "poem-synthesis";
        poemAudioElement.controls = false;
        document.body.appendChild(poemAudioElement);
      });
    }
    return this;
  }
}
