package com.example.onthilaixe.base.extension

fun String.formatTotalQuestion() = String.format("%s %s", this, "câu hỏi")
fun String.formatTitleHome() = String.format("%s %s", "Bộ câu hỏi thi bằng",this)
fun Int.formatTotalQuestionPractice() = String.format("%s", "Gồm $this câu hỏi")

fun String.formatTitleShowResult() = String.format("%s", "Đề $this")
fun String.formatTitleExamResult(unit: String) = String.format("%s", "Kết quả đề thi $this - $unit")