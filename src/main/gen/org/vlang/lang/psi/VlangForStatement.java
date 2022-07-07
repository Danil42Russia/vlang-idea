// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangForStatement extends VlangStatement {

  @Nullable
  VlangExpression getExpression();

  @Nullable
  VlangForClause getForClause();

  @Nullable
  VlangForLabel getForLabel();

  @Nullable
  VlangRangeClause getRangeClause();

  @NotNull
  PsiElement getFor();

}
