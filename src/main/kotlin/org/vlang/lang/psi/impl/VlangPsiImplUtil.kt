package org.vlang.lang.psi.impl

import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.*
import io.ktor.util.*
import org.vlang.configurations.VlangConfiguration
import org.vlang.ide.codeInsight.VlangTypeInfoProvider
import org.vlang.lang.VlangTypes
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.imports.VlangImportReference

object VlangPsiImplUtil {
    @JvmStatic
    fun getName(o: VlangFunctionDeclaration): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun isDefinition(o: VlangFunctionDeclaration): Boolean {
        return o.getBlock() == null
    }

    @JvmStatic
    fun getIdentifier(o: VlangMethodDeclaration): PsiElement? {
        return o.methodName.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangInterfaceDeclaration): PsiElement? {
        return o.interfaceType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangEnumDeclaration): PsiElement? {
        return o.enumType.identifier
    }

    @JvmStatic
    fun getName(o: VlangStructDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangUnionDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangEnumDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangInterfaceDeclaration): String {
        return o.interfaceType.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangConstDefinition): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangTypeAliasDeclaration): String {
        return o.getIdentifier()?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangImportSpec): String {
        return o.importPath.qualifiedName
    }

    @JvmStatic
    fun getQualifiedName(o: VlangImportPath): String {
        return o.importNameList.joinToString(".") { it.text }
    }

    @JvmStatic
    fun getLastPart(o: VlangImportPath): String {
        return o.lastPartPsi.text
    }

    @JvmStatic
    fun getLastPartPsi(o: VlangImportPath): PsiElement {
        return o.importNameList.last()
    }

    @JvmStatic
    fun getLastPart(o: VlangImportSpec): String {
        return o.importPath.lastPart
    }

    @JvmStatic
    fun getLastPartPsi(o: VlangImportSpec): PsiElement {
        return o.importPath.lastPartPsi
    }

    @JvmStatic
    fun getIdentifier(o: VlangStructDeclaration): PsiElement? {
        return o.structType.identifier
    }

    @JvmStatic
    fun getIdentifier(o: VlangType): PsiElement? {
        return o.typeReferenceExpression?.getIdentifier()
    }

    @JvmStatic
    fun getIdentifier(o: VlangFieldName): PsiElement? {
        return o.referenceExpression.getIdentifier()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldName): VlangCompositeElement? {
        return null
    }

    @JvmStatic
    fun getReference(o: VlangReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangImportName): VlangImportReference<VlangImportName> {
        return VlangImportReference(o, o.parentOfType()!!)
    }

    @JvmStatic
    fun resolve(o: VlangImportName): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getNameIdentifier(o: VlangImportName): PsiElement {
        return o.getIdentifier()
    }

    @JvmStatic
    fun getTextOffset(o: VlangImportName): Int {
        return getNameIdentifier(o).textOffset
    }

    @JvmStatic
    fun setName(o: VlangImportName, newName: String): PsiElement {
        val identifier = o.identifier
        identifier.replace(VlangElementFactory.createIdentifierFromText(o.project, newName))
        return o
    }

    @JvmStatic
    fun getName(o: VlangImportName): String? {
        val stub = o.stub
        if (stub != null) {
            return stub.name
        }
        return o.identifier.text
    }

    @JvmStatic
    fun getQualifier(o: VlangImportName): String {
        val parts = mutableListOf<String>()
        var sibling = o.prevSibling
        while (sibling != null) {
            if (sibling is VlangImportName) {
                parts.add(sibling.text)
            }
            sibling = sibling.prevSibling
        }

        return parts.reversed().joinToString(".")
    }

    @JvmStatic
    fun getQualifier(o: VlangReferenceExpression): VlangCompositeElement? {
        return PsiTreeUtil.getChildOfType(o, VlangExpression::class.java)
    }

    @JvmStatic
    fun getQualifier(o: VlangTypeReferenceExpression): VlangCompositeElement? {
        return PsiTreeUtil.getChildOfType(o, VlangTypeReferenceExpression::class.java)
    }

    @JvmStatic
    fun getReceiverType(o: VlangMethodDeclaration): VlangType {
        return o.receiver.type
    }

    fun getTypeReference(o: VlangType?): VlangTypeReferenceExpression? {
        if (o is VlangPointerType) {
            return PsiTreeUtil.findChildOfAnyType(o, VlangTypeReferenceExpression::class.java)
        }
        return o?.typeReferenceExpression
    }

    @JvmStatic
    fun getReadableName(type: VlangType): String {
        if (type is VlangStructType) {
            return type.identifier?.text ?: VlangTypeInfoProvider.UNKNOWN_TYPE
        }

        if (type is VlangEnumType) {
            return type.identifier?.text ?: VlangTypeInfoProvider.UNKNOWN_TYPE
        }

        return type.text?.escapeHTML() ?: VlangTypeInfoProvider.UNKNOWN_TYPE
    }

    @JvmStatic
    fun resolveType(type: VlangType): VlangType? {
        if (type !is VlangTypeImpl) {
            return type
        }

        // TODO:
        val resolved = type.typeReferenceExpression?.resolve()
        val typeChild = resolved?.childrenOfType<VlangStructType>()?.firstOrNull()
        if (typeChild != null) {
            return typeChild
        }

        return type
    }

    @JvmStatic
    fun resolve(o: VlangTypeReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getQualifier(o: VlangFieldDefinition): VlangCompositeElement? {
        return null
    }

    @JvmStatic
    fun resolve(o: VlangReferenceExpression): PsiElement? {
        return o.reference.resolve()
    }

    @JvmStatic
    fun getReference(o: VlangTypeReferenceExpression): VlangReference {
        return VlangReference(o)
    }

    @JvmStatic
    fun getReference(o: VlangLabelRef): VlangLabelReference {
        return VlangLabelReference(o)
    }

    @JvmStatic
    fun getName(o: VlangLabelRef): String {
        return o.identifier.text
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportSpec): PsiElement {
        return o.firstChild
    }

    fun prevDot(e: PsiElement?): Boolean {
        val prev = if (e == null) null else PsiTreeUtil.prevVisibleLeaf(e)
        return prev is LeafElement && (prev as LeafElement).elementType === VlangTypes.DOT
    }

    @JvmStatic
    fun addImport(file: VlangFile, list: VlangImportList?, name: String, alias: String?): VlangImportSpec {
        val decl = VlangElementFactory.createImportDeclaration(file.project, name, alias)!!
        if (list == null) {
            var importList = VlangElementFactory.createImportList(file.project, name, alias)!!
            val modulePsi = file.getModule()

            importList = if (modulePsi == null) {
                file.addBefore(importList, file.firstChild) as VlangImportList
            } else {
                val listPsi = file.addAfter(importList, modulePsi) as VlangImportList
                file.addBefore(VlangElementFactory.createDoubleNewLine(file.project), listPsi)

                listPsi
            }

            return importList.importDeclarationList.first().importSpec!!
        }
        return addImportDeclaration(list, decl)
    }

    private fun addImportDeclaration(importList: VlangImportList, newImportDeclaration: VlangImportDeclaration): VlangImportSpec {
        val lastImport = importList.importDeclarationList.last()
        val importDeclaration = importList.addAfter(newImportDeclaration, lastImport) as VlangImportDeclaration
        val importListNextSibling = importList.nextSibling
        if (importListNextSibling !is PsiWhiteSpace) {
            importList.addAfter(VlangElementFactory.createNewLine(importList.project), importDeclaration)
            if (importListNextSibling != null) {
                // double new line if there is something valuable after import list
                importList.addAfter(VlangElementFactory.createNewLine(importList.project), importDeclaration)
            }
        }
        importList.addBefore(VlangElementFactory.createNewLine(importList.project), importDeclaration)
        return importDeclaration.importSpec!!
    }

    @JvmStatic
    fun getIdentifier(o: VlangImportAlias): PsiElement? {
        return o.importAliasName?.identifier
    }

    @JvmStatic
    fun getReference(o: VlangImportAliasName): VlangImportReference<VlangImportAliasName> {
        return VlangImportReference(o, o.parent.parent as VlangImportSpec)
    }

    @JvmStatic
    fun getName(o: VlangModuleClause): String {
        return o.identifier?.text ?: "<unknown>"
    }

    @JvmStatic
    fun getName(o: VlangImportAlias): String {
        return o.importAliasName?.identifier?.text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangParamDefinition): String {
        return o.getIdentifier().text ?: ""
    }

    @JvmStatic
    fun getName(o: VlangReceiver): String? {
        return o.getIdentifier()?.text
    }

    @JvmStatic
    fun getTypeInner(o: VlangReceiver, context: ResolveState?): VlangType {
        return o.type
    }

    @JvmStatic
    fun getTypeInner(o: VlangEnumDeclaration, context: ResolveState?): VlangType {
        return o.enumType
    }

    @JvmStatic
    fun getUnderlyingType(o: VlangType): PsiElement? {
        return null // TODO
    }

    fun getParentVlangValue(element: PsiElement): VlangValue? {
        var place: PsiElement? = element
        while (PsiTreeUtil.getParentOfType(place, VlangLiteralValueExpression::class.java).also { place = it } != null) {
            if (place?.parent is VlangValue) {
                return place?.parent as? VlangValue
            }
        }
        return null
    }

    fun getFqn(packageName: String?, elementName: String): String {
        return if (StringUtil.isNotEmpty(packageName)) "$packageName.$elementName" else elementName
    }

    @JvmStatic
    fun getType(o: VlangExpression, context: ResolveState?): VlangType? {
        return RecursionManager.doPreventingRecursion(o, true) {
            if (context != null) return@doPreventingRecursion unwrapParType(o, context)

            CachedValuesManager.getCachedValue(o) {
                CachedValueProvider.Result
                    .create(
                        unwrapParType(o, createContextOnElement(o)),
                        PsiModificationTracker.MODIFICATION_COUNT
                    )
            }
        }
    }

    val CONTEXT = Key.create<SmartPsiElementPointer<PsiElement>>("CONTEXT")

    fun createContextOnElement(element: PsiElement): ResolveState {
        return ResolveState.initial().put(
            CONTEXT,
            SmartPointerManager.getInstance(element.project).createSmartPsiElementPointer(element)
        )
    }

    private fun unwrapParType(o: VlangExpression, c: ResolveState?): VlangType? {
        val inner = getTypeInner(o, c)
        return /*if (inner is VlangParType) (inner as VlangParType).getActualType() else*/ inner
    }

    private fun getTypeInner(expr: VlangExpression, context: ResolveState?): VlangType? {
        if (expr is VlangUnaryExpr) {
            val e = expr.expression ?: return null
            val type = e.getType(context)
            val base = if (type == null/* || type.getTypeReferenceExpression() == null*/) type else type.underlyingType
//            if (o.bitAnd != null) return LightPointerType(type)
            if (expr.sendChannel != null) return if (base is VlangChannelType) base.type else type
            return if (expr.mul != null) if (base is VlangPointerType) base.type else type else type
        }
        if (expr is VlangAddExpr) {
            return expr.left.getType(context)
        }
        if (expr is VlangMulExpr) {
            val left = expr.left
            if (left !is VlangLiteral) return left.getType(context)
            val right = (expr as VlangBinaryExpr).right
            if (right != null) return right.getType(context)
        } else if (expr is VlangReferenceExpression) {
            if (VlangCompletionUtil.isCompileTimeIdentifier(expr.getIdentifier())) {
                return getBuiltinType("string", expr)
            }

            // expr or { err }
            if (expr.getIdentifier().text == "err" && expr.parentOfType<VlangOrBlockExpr>() != null) {
                return getBuiltinType("IError", expr)
            }

            val reference = expr.reference
            val resolve = reference.resolve()
            if (resolve is VlangTypeOwner)
                return typeOrParameterType(resolve, context)
        } else if (expr is VlangParenthesesExpr) {
            return expr.expression?.getType(context)
        } else if (expr is VlangStringLiteral) {
            return getBuiltinType("string", expr)
        } else if (expr is VlangLiteral) {
            if (expr.char != null) return getBuiltinType("rune", expr)
            if (expr.int != null || expr.hex != null || expr.oct != null)
                return getBuiltinType("i64", expr)
            if (expr.float != null) return getBuiltinType("f64", expr)
            if (expr.floati != null) return getBuiltinType("complex64", expr)
            if (expr.decimali != null) return getBuiltinType("complex128", expr)
        } else if (expr is VlangConditionalExpr) {
            return getBuiltinType("bool", expr)
        }

        if (expr is VlangIndexOrSliceExpr) {
            val inner = expr.expressionList.firstOrNull()?.getType(null)
            if (inner is VlangArrayOrSliceType) {
                return inner.type
            }
            if (inner is VlangMapType) {
                return inner.valueType
            }
        }

        if (expr is VlangLiteralValueExpression) {
            val type = expr.type.typeReferenceExpression?.resolve()
            if (type?.firstChild is VlangStructType) {
                return type.firstChild as VlangStructType
            }
            return expr.type
        }

        if (expr is VlangCallExpr) {
            val callRef = expr.expression as? VlangReferenceExpression
            val resolved = callRef?.reference?.resolve()
            if (resolved !is VlangFunctionOrMethodDeclaration) return null

            val result = resolved.getSignature()?.result ?: return null
            return result.type
        }

        return null
    }

    private fun typeOrParameterType(resolve: VlangTypeOwner, context: ResolveState?): VlangType? {
        val type = resolve.getType(context)
//        if (resolve is VlangParamDefinition && (resolve as VlangParamDefinition).isVariadic()) {
//            return if (type == null) null else LightArrayType(type)
//        }
//        return if (resolve is VlangSignatureOwner) {
//            LightFunctionType(resolve as VlangSignatureOwner)
//        } else type
        return type
    }

    @JvmStatic
    fun getSymbolVisibility(o: VlangVarDefinition): VlangSymbolVisibility? {
        return null
    }

    @JvmStatic
    fun getSymbolVisibility(o: VlangConstDefinition): VlangSymbolVisibility? {
        return (o.parent as? VlangConstDeclaration)?.symbolVisibility
    }

    @JvmStatic
    fun getName(o: VlangVarDefinition): String {
        return o.getIdentifier().text
    }

    @JvmStatic
    fun getReference(o: VlangVarDefinition): PsiReference? {
        val createRef = PsiTreeUtil.getParentOfType(
            o,
            VlangBlock::class.java,
            VlangForStatement::class.java,
            VlangIfStatement::class.java,
//            VlangSwitchStatement::class.java,
//            VlangSelectStatement::class.java
        ) is VlangBlock
        return if (createRef) VlangVarReference(o) else null
    }

    fun getBuiltinType(name: String, context: PsiElement): VlangType? {
        val builtin = VlangConfiguration.getInstance(context.project).builtinLocation
        if (builtin != null) {
            print("")
        }

        val file = VlangElementFactory.createFileFromText(context.project, "fn f(a $name)")

        val type = file.findElementAt(9)?.parent?.parent as? VlangType

        return type
    }

    @JvmStatic
    fun getTypeInner(o: VlangVarDefinition, context: ResolveState?): VlangType? {
        val parent = PsiTreeUtil.getStubOrPsiParent(o)
//        if (parent is VlangRangeClause) {
//            return processRangeClause(o, parent as VlangRangeClause?, context)
//        }
        if (parent is VlangVarDeclaration) {
            return getTypeInVarSpec(o, parent, context)
        }
        val literal = PsiTreeUtil.getNextSiblingOfType(o, VlangLiteral::class.java)
        if (literal != null) {
            return literal.getType(context)
        }
//        val siblingType: VlangType = o.findSiblingType()
//        if (siblingType != null) return siblingType
//        if (parent is VlangTypeSwitchGuard) {
//            val switchStatement: VlangTypeSwitchStatement = ObjectUtils.tryCast(parent!!.parent, VlangTypeSwitchStatement::class.java)
//            if (switchStatement != null) {
//                val typeCase: VlangTypeCaseClause = getTypeCaseClause(
//                    getContextElement(context),
//                    switchStatement
//                )
//                return if (typeCase != null) {
//                    if (typeCase.getDefault() != null) (parent as VlangTypeSwitchGuard?).getExpression()
//                        .getVlangType(context) else typeCase.getType()
//                } else (parent as VlangTypeSwitchGuard?).getExpression().getVlangType(null)
//            }
//        }
        return null
    }

    private fun getTypeInVarSpec(o: VlangVarDefinition, decl: VlangVarDeclaration, context: ResolveState?): VlangType? {
        val defineIndex = decl.varDefinitionList.indexOf(o)
        val exprList = decl.expressionList

        // if a, b = call()
        if (exprList.size == 1) {
            val expr = exprList.first()
            val type = expr.getType(context)
            if (type is VlangTupleType) {
                return type.typeListNoPin.typeList.getOrNull(defineIndex)
            }
        }

        // if a, b := 10, 20
        val neededExpr = exprList.getOrNull(defineIndex)
        if (neededExpr != null) {
            return neededExpr.getType(context)
        }

        return null
    }

    fun processSignatureOwner(o: VlangSignatureOwner, processor: VlangScopeProcessorBase): Boolean {
        val signature = o.getSignature() ?: return true
        if (!processParameters(processor, signature.parameters)) {
            return false
        }

        return true
    }

    private fun processParameters(processor: VlangScopeProcessorBase, parameters: VlangParameters): Boolean {
        for (declaration in parameters.parameterDeclarationList) {
            if (!processNamedElements(
                    processor,
                    ResolveState.initial(),
                    declaration.paramDefinitionList,
                    true
                )
            ) {
                return false
            }
        }
        return true
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        localResolve: Boolean,
    ): Boolean {
        return processNamedElements(processor, state, elements, Conditions.alwaysTrue(), localResolve, false)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        localResolve: Boolean,
        checkContainingFile: Boolean,
    ): Boolean {
        return processNamedElements(processor, state, elements, Conditions.alwaysTrue(), localResolve, checkContainingFile)
    }

    fun processNamedElements(
        processor: PsiScopeProcessor,
        state: ResolveState,
        elements: Collection<VlangNamedElement>,
        condition: Condition<Any>,
        localResolve: Boolean,
        checkContainingFile: Boolean,
    ): Boolean {
        val contextFile = if (checkContainingFile) VlangReference.getContextFile(state) else null
        val module = if (contextFile != null) ModuleUtilCore.findModuleForPsiElement(contextFile) else null
        for (definition in elements) {
            if (!condition.value(definition)) continue
            if (!definition.isValid() || checkContainingFile
            /*&& !VlangPsiImplUtil.allowed(
                definition.getContainingFile(),
                contextFile,
                module
            )*/
            ) continue
            if ((localResolve || definition.isPublic()) && !processor.execute(definition, state))
                return false
        }
        return true
    }

    fun traverser(): SyntaxTraverser<PsiElement?> {
        return SyntaxTraverser.psiTraverser()
            .forceDisregardTypes(Conditions.equalTo(GeneratedParserUtilBase.DUMMY_BLOCK))
    }

    fun canBeAutoImported(file: VlangFile, allowMain: Boolean, module: Module?): Boolean {
        return file.getModuleName() != "main"
//        return if (VlangPsiImplUtil.isBuiltinFile(file) || !allowMain && file.packageName == "main") {
//            false
//        } else VlangPsiImplUtil.allowed(file, null, module) && !VlangUtil.isExcludedFile(file)
    }
}