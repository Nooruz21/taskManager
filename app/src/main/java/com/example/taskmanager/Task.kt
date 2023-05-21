package com.example.taskmanager

data class Task(
    var data: List<Data?>? = null,
    var status: String? = null,
    var total: Int? = null
) {
    data class Data(
        var id: Int? = null,
        var text: String? = null
    )
}