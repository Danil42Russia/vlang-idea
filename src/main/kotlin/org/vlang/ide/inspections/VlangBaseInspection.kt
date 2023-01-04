package org.vlang.ide.inspections

import com.intellij.codeInspection.CustomSuppressableInspectionTool
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.SuppressIntentionAction
import com.intellij.codeInspection.SuppressIntentionActionFromFix
import com.intellij.psi.PsiElement
import org.vlang.ide.inspections.suppression.VlangInspectionSuppressor

abstract class VlangBaseInspection : LocalInspectionTool(), CustomSuppressableInspectionTool {
    override fun getSuppressActions(element: PsiElement?): Array<SuppressIntentionAction?> {
        return SuppressIntentionActionFromFix.convertBatchToSuppressIntentionActions(
            VlangInspectionSuppressor().getSuppressActions(element, id)
        )
    }
}
