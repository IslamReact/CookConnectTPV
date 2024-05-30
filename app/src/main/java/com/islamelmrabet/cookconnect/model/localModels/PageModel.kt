package com.islamelmrabet.cookconnect.model.localModels

/**
 * Class: PageModel
 *
 * @property title
 * @property animationFile
 * @property description
 */
data class PageModel(
    val title: String,
    val animationFile: String,
    val description: String = ""
)