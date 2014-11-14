package me.montecode.pmcg.kvizoprirodi;

/**
 * Created by stevyhacker on 23.7.14..
 */
public class QuestionItem {

    int id ;
    String question, answer, option1,option2,option3;

    public QuestionItem(){

    }

    public QuestionItem(int id, String question, String answer, String option1, String option2, String option3){
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;

    }

    public int getId() {
        return id;
    }
}
