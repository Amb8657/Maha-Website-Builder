package com.maha.builder.engine

import com.maha.builder.data.WebNode

object HtmlEngine {
    fun compileNodes(nodes: List<WebNode>): String {
        return nodes.joinToString("\n") { node ->
            val base = "class='maha-element'"
            when (node.type) {
                "HEADER" -> "<h1 $base style='font-size:36px; color:#111; margin:0 0 16px 0;'>Heading Text</h1>"
                "SUBHEADER" -> "<h2 $base style='font-size:24px; color:#444; margin:0 0 12px 0;'>Subheading Text</h2>"
                "PARAGRAPH" -> "<p $base style='font-size:16px; color:#666; line-height:1.6; margin:0 0 20px 0;'>This is a professional paragraph block. Tap to edit this text and add your own content.</p>"
                "BUTTON_PRIMARY" -> "<div $base style='margin-bottom:20px;'><button style='background:#D4AF37; color:#1A0505; border:none; padding:14px 28px; border-radius:8px; font-weight:bold; font-size:16px; cursor:pointer;'>Click Here</button></div>"
                "BUTTON_OUTLINE" -> "<div $base style='margin-bottom:20px;'><button style='background:transparent; color:#D4AF37; border:2px solid #D4AF37; padding:12px 26px; border-radius:8px; font-weight:bold; font-size:16px; cursor:pointer;'>Learn More</button></div>"
                "CARD" -> "<div $base style='background:#fff; border-radius:12px; padding:24px; box-shadow:0 4px 15px rgba(0,0,0,0.05); margin-bottom:20px; border:1px solid #eee;'><h3 style='margin:0 0 10px 0; color:#111;'>Card Title</h3><p style='margin:0; color:#666;'>Card description content goes here.</p></div>"
                else -> "<div $base>Unknown Element</div>"
            }
        }
    }
}
