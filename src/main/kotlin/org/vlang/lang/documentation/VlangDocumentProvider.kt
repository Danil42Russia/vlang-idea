package org.vlang.lang.documentation

import com.intellij.codeInsight.highlighting.HighlightManager
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.psi.PsiElement
import org.vlang.lang.psi.VlangFunctionDeclaration
import org.vlang.lang.psi.VlangReferenceExpression

class VlangDocumentProvider : AbstractDocumentationProvider() {
    // when hovering with cmd — very short, one line info
    override fun getQuickNavigateInfo(element: PsiElement, hoverElement: PsiElement): String? {
        if (element is VlangReferenceExpression) {
            val res = element.reference.multiResolve(false).firstOrNull() ?: return null

            return generateModuleShortInfo(res.element)
        }

        return generateModuleShortInfo(element)

//        if (element is JsonStringLiteral) {
//            return generateComposerPackageShortInfo(element)
//        }

//        return null
    }

    private fun generateModuleShortInfo(element: PsiElement?): String? {
        if (element is VlangFunctionDeclaration) {
            val infoBuf = HtmlBuilder()

            val name = element.getIdentifier().text
            val signature = element.getSignature()?.text

            infoBuf.appendRaw("<code>")
            infoBuf.appendRaw("fn $name$signature")
            infoBuf.appendRaw("</code>\n")
            return infoBuf.toString()
        }

        return null
    }


    // when just hovering — a bit more detailed (but still only most important) info
    override fun generateDoc(element: PsiElement, hoverElement: PsiElement?): String? {
//        if (element is ModuleNamePsi) {
//            return generateModuleDoc(element)
//        }
//
//        if (element is JsonStringLiteral) {
//            return generateComposerPackageDoc(element)
//        }

        return null
    }
//
//    private fun generateModuleDoc(element: ModuleNamePsi): String? {
//        val module = element.module() ?: return null
//
//        val buffer = StringBuilder()
//
//        buffer.append("<div class='definition'><pre>")
//        buffer.append("<b>Module ${module.name}</b>")
//        buffer.append("</pre></div>")
//
//        buffer.append("<table class='sections'>")
//
//        addSection(buffer, "Name:", module.name)
//        addSection(buffer, "Namespace:", module.namespace.toString())
//        addSection(buffer, "Description:", module.description)
//
//        val moduleDir = module.getModuleDir()
//        addSource(element.project, buffer, moduleDir)
//
//        buffer.append("</table>")
//
//        return buffer.toString().ifEmpty { null }
//    }
//
//    private fun generateComposerPackageDoc(element: JsonStringLiteral): String? {
//        val composerPackage = element.composerPackage() ?: return null
//        val name = composerPackage.name.removePrefix("#")
//
//        val buffer = StringBuilder()
//
//        buffer.append("<div class='definition'><pre>")
//        buffer.append("<b>Composer package $name</b>")
//        buffer.append("</pre></div>")
//
//        buffer.append("<table class='sections'>")
//
//        addSection(buffer, "Name:", name)
//        addSection(buffer, "Description:", composerPackage.description)
//
//        val packageDir = composerPackage.getPackageDir()
//        addSource(element.project, buffer, packageDir)
//
//        buffer.append("</table>")
//
//        return buffer.toString().ifEmpty { null }
//    }
//
//    private fun addSource(
//        project: Project,
//        buffer: StringBuilder,
//        dir: VirtualFile?,
//    ) {
//        if (dir == null) return
//
//        val sourceLoc = PhpPresentationUtil.getPresentablePathForFile(dir, project)
//        if (".../" != sourceLoc) {
//            addSection(buffer, "Source:", "<span path=\"\">$sourceLoc</span>")
//        }
//    }
//
//    private fun addSection(buffer: StringBuilder, name: String, content: String?) {
//        if (StringUtil.isNotEmpty(content)) {
//            buffer.append("<tr><td valign='top' class='section'><p>")
//            buffer.append(name)
//            buffer.append("</td><td valign='top'>")
//            buffer.append(content)
//            buffer.append("</td>")
//        }
//    }
}
