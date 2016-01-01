package me.montecode.simplequizapp.tehno;

/**
 * Created by stevyhacker on 23.7.14..
 */
public class QuestionItem {

    int id, level;
    String question, answer, option1, option2, option3, imageName;

    public QuestionItem() {

    }

    public QuestionItem(int id, String question, int level, String answer, String option1, String option2, String option3, String imageName) {
        this.id = id;
        this.question = question;
        this.level = level;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }
}
