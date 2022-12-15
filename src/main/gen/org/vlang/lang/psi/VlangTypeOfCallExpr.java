// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vlang.lang.psi.impl.VlangBuiltinReference;

public interface VlangTypeOfCallExpr extends VlangExpression, VlangBuiltinCallOwner {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangGenericArguments getGenericArguments();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getTypeof();

  @NotNull
  VlangBuiltinReference<VlangTypeOfCallExpr> getReference();

}
