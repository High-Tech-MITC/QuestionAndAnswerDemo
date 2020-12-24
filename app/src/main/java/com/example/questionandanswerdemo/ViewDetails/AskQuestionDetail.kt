package com.example.questionandanswerdemo.ViewDetails

import java.sql.Time
import java.sql.Timestamp

class AskQuestionDetail(var question:String,
                        val time:Map<String,String>,
                         var userName:String,
                         var userimage:String,
                         var type:String,
                        var email:String,
                        var uid:String,
                         var answers:Int = 0,
                         var views:Int=0,
                            var likes:Int=0) {
}