package com.maha.builder.engine

import com.maha.builder.data.WebNode

object HtmlEngine {
    // Converts internal database models into a real, raw HTML website
    fun compileWebsite(nodes: List<WebNode>): String {
        val bodyContent = nodes.joinToString("\n") { node ->
            when (node.type) {
                "HEADER" -> "<h1 style='${node.cssRules}'>${node.content}</h1>"
                "PARAGRAPH" -> "<p style='${node.cssRules}'>${node.content}</p>"
                "BUTTON" -> "<button style='${node.cssRules}'>${node.content}</button>"
                else -> "<div style='${node.cssRules}'>${node.content}</div>"
            }
        }
        
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: sans-serif; margin: 0; padding: 20px; background: #FFFFFF; }
                    .element:hover { border: 2px dashed #D4AF37; cursor: pointer; }
                </style>
            </head>
            <body>
                $bodyContent
            </body>
            </html>
        """.trimIndent()
    }
}
