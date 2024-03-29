package org.vlang.lang.completion.providers

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.completion.VlangCompletionUtil.withPriority
import org.vlang.lang.psi.VlangFile

object SpecialTestFunctionsCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val file = parameters.originalFile as? VlangFile ?: return
        if (!file.isTestFile()) return

        val functions = file.getFunctions()
        val containsTestSuiteBegin = functions.any { it.name == "testsuite_begin" }
        val containsTestSuiteEnd = functions.any { it.name == "testsuite_end" }

        if (!containsTestSuiteBegin) {
            result.addElement(fnPrefixedElement("testsuite_begin"))
        }

        if (!containsTestSuiteEnd) {
            result.addElement(fnPrefixedElement("testsuite_end"))
        }
    }

    private fun fnPrefixedElement(name: String) =
        LookupElementBuilder.create("fn $name")
            .withTailText("() {...}")
            .withInsertHandler(VlangCompletionUtil.TemplateStringInsertHandler("() {\n\t\$END$\n}"))
            .bold()
            .withPriority(VlangCompletionUtil.CONTEXT_COMPLETION_PRIORITY)
}
