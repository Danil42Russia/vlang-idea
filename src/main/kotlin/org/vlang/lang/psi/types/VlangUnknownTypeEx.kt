package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangType

class VlangUnknownTypeEx(raw: VlangType?): VlangBaseTypeEx<VlangType?>(raw) {
    override fun toString(): String = "any"

    override fun readableName(context: VlangCompositeElement): String = "any"

    override fun isAssignableFrom(rhs: VlangTypeEx<*>, project: Project): Boolean {
        return true
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }
    }
}
