package com.lisners.counsellor.ApiModal;

public class ModelFaq {
    int id , active ;
    String question ,answer ;

    public ModelFaq(int id, int active, String faq_question, String faq_answer) {
        this.id = id;
        this.active = active;
        this.question = faq_question;
        this.answer = faq_answer;
    }

    public int getId() {
        return id;
    }

    public int getActive() {
        return active;
    }

    public String getFaq_question() {
        return question;
    }

    public String getFaq_answer() {
        return answer;
    }
}
