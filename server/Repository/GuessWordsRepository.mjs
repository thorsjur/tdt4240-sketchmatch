import { GuessWord } from '../Models/GuessWord.mjs';
import fs from 'fs';
import path from 'path';

export class GuessWordsRepository {
    constructor() {
        if (!GuessWordsRepository.instance) {
            this.words = [];
            GuessWordsRepository.instance = this;
        }

        if(this.words.length === 0) {
            // Read words from file
            const __dirname = path.resolve();
            const filePath = path.join(__dirname, '/words.json');
            const data = fs.readFileSync(filePath, 'utf8');
            const words = JSON.parse(data);
            // For each
            words.forEach(word => {
                this.words.push(new GuessWord(word.id, word.word, word.difficulty));
            });
        }

        return GuessWordsRepository.instance;
    }

    // Get words
    getWords() {
        return this.words;
    }

    // Get word by id
    getWordById(id) {
        return this.words.find(word => word.id === id);
    }

    // Get 3 random words. 1 easy, 1 medium, 1 hard
    getRandomWords() {
        const easyWords = this.words.filter(word => word.difficulty === 0);
        const mediumWords = this.words.filter(word => word.difficulty === 1);
        const hardWords = this.words.filter(word => word.difficulty === 2);

        const randomEasyWord = easyWords[Math.floor(Math.random() * easyWords.length)];
        const randomMediumWord = mediumWords[Math.floor(Math.random() * mediumWords.length)];
        const randomHardWord = hardWords[Math.floor(Math.random() * hardWords.length)];

        return [randomEasyWord, randomMediumWord, randomHardWord];
    }



    
}