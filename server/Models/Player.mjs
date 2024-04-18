export class Player {
    constructor(id, hwid, name) {
        this.id = id;
        this.hwid = hwid;
        this.nickname = name;
        this.score = 0;
        this.isDrawing = false;
    }

    // Set nickname
    setNickname(nickname) {
        this.nickname = nickname;
    }

    // Update score
    setScore(score) {
        this.score = score;
    }

    // Update score
    getScore() {
        return this.score;
    }

    getIsDrawing() {
        return this.isDrawing;
    }

    setIsDrawing(isDrawing) {
        this.isDrawing = isDrawing;
    }
}
