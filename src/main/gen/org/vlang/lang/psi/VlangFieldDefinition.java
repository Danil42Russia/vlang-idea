// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.stubs.VlangFieldDefinitionStub;

public interface VlangFieldDefinition extends VlangNamedElement, StubBasedPsiElement<VlangFieldDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  VlangReferenceExpression getQualifier();

}