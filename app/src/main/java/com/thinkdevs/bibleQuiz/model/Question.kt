package com.thinkdevs.bibleQuiz.model

class Question (var question:String?=null,
                var optionA:String?=null,
                var optionB:String?=null,
                var optionC:String?=null,
                var optionD:String?=null,
                var correctAns:String?=null,
                var setNo:Int=0
){
    override fun toString(): String {
        return "Question(question=$question," +
                " optionA=$optionA, " +
                "optionB=$optionB, optionC=$optionC, " +
                "optionD=$optionD, correctAns=$correctAns, setNo=$setNo)"
    }
}

