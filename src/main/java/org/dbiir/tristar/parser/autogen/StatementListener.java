package org.dbiir.tristar.parser.autogen;// Generated from /Users/andrew/2024/tristar/src/main/antlr4/Statement.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StatementParser}.
 */
public interface StatementListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StatementParser#execute}.
	 * @param ctx the parse tree
	 */
	void enterExecute(StatementParser.ExecuteContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#execute}.
	 * @param ctx the parse tree
	 */
	void exitExecute(StatementParser.ExecuteContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterStatement}.
	 * @param ctx the parse tree
	 */
	void enterAlterStatement(StatementParser.AlterStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterStatement}.
	 * @param ctx the parse tree
	 */
	void exitAlterStatement(StatementParser.AlterStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTable}.
	 * @param ctx the parse tree
	 */
	void enterCreateTable(StatementParser.CreateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTable}.
	 * @param ctx the parse tree
	 */
	void exitCreateTable(StatementParser.CreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionClause}.
	 * @param ctx the parse tree
	 */
	void enterPartitionClause(StatementParser.PartitionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionClause}.
	 * @param ctx the parse tree
	 */
	void exitPartitionClause(StatementParser.PartitionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionTypeDef}.
	 * @param ctx the parse tree
	 */
	void enterPartitionTypeDef(StatementParser.PartitionTypeDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionTypeDef}.
	 * @param ctx the parse tree
	 */
	void exitPartitionTypeDef(StatementParser.PartitionTypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#subPartitions}.
	 * @param ctx the parse tree
	 */
	void enterSubPartitions(StatementParser.SubPartitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#subPartitions}.
	 * @param ctx the parse tree
	 */
	void exitSubPartitions(StatementParser.SubPartitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionKeyAlgorithm}.
	 * @param ctx the parse tree
	 */
	void enterPartitionKeyAlgorithm(StatementParser.PartitionKeyAlgorithmContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionKeyAlgorithm}.
	 * @param ctx the parse tree
	 */
	void exitPartitionKeyAlgorithm(StatementParser.PartitionKeyAlgorithmContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#duplicateAsQueryExpression}.
	 * @param ctx the parse tree
	 */
	void enterDuplicateAsQueryExpression(StatementParser.DuplicateAsQueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#duplicateAsQueryExpression}.
	 * @param ctx the parse tree
	 */
	void exitDuplicateAsQueryExpression(StatementParser.DuplicateAsQueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void enterAlterTable(StatementParser.AlterTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTable}.
	 * @param ctx the parse tree
	 */
	void exitAlterTable(StatementParser.AlterTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#standaloneAlterTableAction}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneAlterTableAction(StatementParser.StandaloneAlterTableActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#standaloneAlterTableAction}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneAlterTableAction(StatementParser.StandaloneAlterTableActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTableActions}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableActions(StatementParser.AlterTableActionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTableActions}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableActions(StatementParser.AlterTableActionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTablePartitionOptions}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablePartitionOptions(StatementParser.AlterTablePartitionOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTablePartitionOptions}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablePartitionOptions(StatementParser.AlterTablePartitionOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterCommandList}.
	 * @param ctx the parse tree
	 */
	void enterAlterCommandList(StatementParser.AlterCommandListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterCommandList}.
	 * @param ctx the parse tree
	 */
	void exitAlterCommandList(StatementParser.AlterCommandListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterList}.
	 * @param ctx the parse tree
	 */
	void enterAlterList(StatementParser.AlterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterList}.
	 * @param ctx the parse tree
	 */
	void exitAlterList(StatementParser.AlterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTableOptionsSpaceSeparated}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableOptionsSpaceSeparated(StatementParser.CreateTableOptionsSpaceSeparatedContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTableOptionsSpaceSeparated}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableOptionsSpaceSeparated(StatementParser.CreateTableOptionsSpaceSeparatedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAddColumn(StatementParser.AddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAddColumn(StatementParser.AddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addTableConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAddTableConstraint(StatementParser.AddTableConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addTableConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAddTableConstraint(StatementParser.AddTableConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code changeColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterChangeColumn(StatementParser.ChangeColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code changeColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitChangeColumn(StatementParser.ChangeColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code modifyColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterModifyColumn(StatementParser.ModifyColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code modifyColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitModifyColumn(StatementParser.ModifyColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableDrop}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableDrop(StatementParser.AlterTableDropContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableDrop}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableDrop(StatementParser.AlterTableDropContext ctx);
	/**
	 * Enter a parse tree produced by the {@code disableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterDisableKeys(StatementParser.DisableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code disableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitDisableKeys(StatementParser.DisableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code enableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterEnableKeys(StatementParser.EnableKeysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code enableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitEnableKeys(StatementParser.EnableKeysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterColumn(StatementParser.AlterColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterColumn(StatementParser.AlterColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterIndex(StatementParser.AlterIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterIndex(StatementParser.AlterIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterCheck}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterCheck(StatementParser.AlterCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterCheck}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterCheck(StatementParser.AlterCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterConstraint(StatementParser.AlterConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterConstraint(StatementParser.AlterConstraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterRenameColumn(StatementParser.RenameColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitRenameColumn(StatementParser.RenameColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterRenameTable}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterRenameTable(StatementParser.AlterRenameTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterRenameTable}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterRenameTable(StatementParser.AlterRenameTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code renameIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterRenameIndex(StatementParser.RenameIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code renameIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitRenameIndex(StatementParser.RenameIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterConvert}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterConvert(StatementParser.AlterConvertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterConvert}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterConvert(StatementParser.AlterConvertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableForce}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableForce(StatementParser.AlterTableForceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableForce}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableForce(StatementParser.AlterTableForceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterTableOrder}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void enterAlterTableOrder(StatementParser.AlterTableOrderContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterTableOrder}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 */
	void exitAlterTableOrder(StatementParser.AlterTableOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterOrderList}.
	 * @param ctx the parse tree
	 */
	void enterAlterOrderList(StatementParser.AlterOrderListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterOrderList}.
	 * @param ctx the parse tree
	 */
	void exitAlterOrderList(StatementParser.AlterOrderListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableConstraintDef}.
	 * @param ctx the parse tree
	 */
	void enterTableConstraintDef(StatementParser.TableConstraintDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableConstraintDef}.
	 * @param ctx the parse tree
	 */
	void exitTableConstraintDef(StatementParser.TableConstraintDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterCommandsModifierList}.
	 * @param ctx the parse tree
	 */
	void enterAlterCommandsModifierList(StatementParser.AlterCommandsModifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterCommandsModifierList}.
	 * @param ctx the parse tree
	 */
	void exitAlterCommandsModifierList(StatementParser.AlterCommandsModifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterCommandsModifier}.
	 * @param ctx the parse tree
	 */
	void enterAlterCommandsModifier(StatementParser.AlterCommandsModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterCommandsModifier}.
	 * @param ctx the parse tree
	 */
	void exitAlterCommandsModifier(StatementParser.AlterCommandsModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#withValidation}.
	 * @param ctx the parse tree
	 */
	void enterWithValidation(StatementParser.WithValidationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#withValidation}.
	 * @param ctx the parse tree
	 */
	void exitWithValidation(StatementParser.WithValidationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#standaloneAlterCommands}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneAlterCommands(StatementParser.StandaloneAlterCommandsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#standaloneAlterCommands}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneAlterCommands(StatementParser.StandaloneAlterCommandsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterPartition}.
	 * @param ctx the parse tree
	 */
	void enterAlterPartition(StatementParser.AlterPartitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterPartition}.
	 * @param ctx the parse tree
	 */
	void exitAlterPartition(StatementParser.AlterPartitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#constraintClause}.
	 * @param ctx the parse tree
	 */
	void enterConstraintClause(StatementParser.ConstraintClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#constraintClause}.
	 * @param ctx the parse tree
	 */
	void exitConstraintClause(StatementParser.ConstraintClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableElementList}.
	 * @param ctx the parse tree
	 */
	void enterTableElementList(StatementParser.TableElementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableElementList}.
	 * @param ctx the parse tree
	 */
	void exitTableElementList(StatementParser.TableElementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void enterTableElement(StatementParser.TableElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableElement}.
	 * @param ctx the parse tree
	 */
	void exitTableElement(StatementParser.TableElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#restrict}.
	 * @param ctx the parse tree
	 */
	void enterRestrict(StatementParser.RestrictContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#restrict}.
	 * @param ctx the parse tree
	 */
	void exitRestrict(StatementParser.RestrictContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fulltextIndexOption}.
	 * @param ctx the parse tree
	 */
	void enterFulltextIndexOption(StatementParser.FulltextIndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fulltextIndexOption}.
	 * @param ctx the parse tree
	 */
	void exitFulltextIndexOption(StatementParser.FulltextIndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void enterDropTable(StatementParser.DropTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropTable}.
	 * @param ctx the parse tree
	 */
	void exitDropTable(StatementParser.DropTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void enterDropIndex(StatementParser.DropIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropIndex}.
	 * @param ctx the parse tree
	 */
	void exitDropIndex(StatementParser.DropIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterAlgorithmOption}.
	 * @param ctx the parse tree
	 */
	void enterAlterAlgorithmOption(StatementParser.AlterAlgorithmOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterAlgorithmOption}.
	 * @param ctx the parse tree
	 */
	void exitAlterAlgorithmOption(StatementParser.AlterAlgorithmOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterLockOption}.
	 * @param ctx the parse tree
	 */
	void enterAlterLockOption(StatementParser.AlterLockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterLockOption}.
	 * @param ctx the parse tree
	 */
	void exitAlterLockOption(StatementParser.AlterLockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void enterTruncateTable(StatementParser.TruncateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#truncateTable}.
	 * @param ctx the parse tree
	 */
	void exitTruncateTable(StatementParser.TruncateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndex(StatementParser.CreateIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createIndex}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndex(StatementParser.CreateIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabase(StatementParser.CreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createDatabase}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabase(StatementParser.CreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void enterAlterDatabase(StatementParser.AlterDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterDatabase}.
	 * @param ctx the parse tree
	 */
	void exitAlterDatabase(StatementParser.AlterDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createDatabaseSpecification_}.
	 * @param ctx the parse tree
	 */
	void enterCreateDatabaseSpecification_(StatementParser.CreateDatabaseSpecification_Context ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createDatabaseSpecification_}.
	 * @param ctx the parse tree
	 */
	void exitCreateDatabaseSpecification_(StatementParser.CreateDatabaseSpecification_Context ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterDatabaseSpecification_}.
	 * @param ctx the parse tree
	 */
	void enterAlterDatabaseSpecification_(StatementParser.AlterDatabaseSpecification_Context ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterDatabaseSpecification_}.
	 * @param ctx the parse tree
	 */
	void exitAlterDatabaseSpecification_(StatementParser.AlterDatabaseSpecification_Context ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void enterDropDatabase(StatementParser.DropDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropDatabase}.
	 * @param ctx the parse tree
	 */
	void exitDropDatabase(StatementParser.DropDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void enterAlterInstance(StatementParser.AlterInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterInstance}.
	 * @param ctx the parse tree
	 */
	void exitAlterInstance(StatementParser.AlterInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#instanceAction}.
	 * @param ctx the parse tree
	 */
	void enterInstanceAction(StatementParser.InstanceActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#instanceAction}.
	 * @param ctx the parse tree
	 */
	void exitInstanceAction(StatementParser.InstanceActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#channel}.
	 * @param ctx the parse tree
	 */
	void enterChannel(StatementParser.ChannelContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#channel}.
	 * @param ctx the parse tree
	 */
	void exitChannel(StatementParser.ChannelContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void enterCreateEvent(StatementParser.CreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createEvent}.
	 * @param ctx the parse tree
	 */
	void exitCreateEvent(StatementParser.CreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void enterAlterEvent(StatementParser.AlterEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterEvent}.
	 * @param ctx the parse tree
	 */
	void exitAlterEvent(StatementParser.AlterEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void enterDropEvent(StatementParser.DropEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropEvent}.
	 * @param ctx the parse tree
	 */
	void exitDropEvent(StatementParser.DropEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateFunction(StatementParser.CreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateFunction(StatementParser.CreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void enterAlterFunction(StatementParser.AlterFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterFunction}.
	 * @param ctx the parse tree
	 */
	void exitAlterFunction(StatementParser.AlterFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void enterDropFunction(StatementParser.DropFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropFunction}.
	 * @param ctx the parse tree
	 */
	void exitDropFunction(StatementParser.DropFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcedure(StatementParser.CreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createProcedure}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcedure(StatementParser.CreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void enterAlterProcedure(StatementParser.AlterProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterProcedure}.
	 * @param ctx the parse tree
	 */
	void exitAlterProcedure(StatementParser.AlterProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void enterDropProcedure(StatementParser.DropProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropProcedure}.
	 * @param ctx the parse tree
	 */
	void exitDropProcedure(StatementParser.DropProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createServer}.
	 * @param ctx the parse tree
	 */
	void enterCreateServer(StatementParser.CreateServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createServer}.
	 * @param ctx the parse tree
	 */
	void exitCreateServer(StatementParser.CreateServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void enterAlterServer(StatementParser.AlterServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterServer}.
	 * @param ctx the parse tree
	 */
	void exitAlterServer(StatementParser.AlterServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void enterDropServer(StatementParser.DropServerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropServer}.
	 * @param ctx the parse tree
	 */
	void exitDropServer(StatementParser.DropServerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createView}.
	 * @param ctx the parse tree
	 */
	void enterCreateView(StatementParser.CreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createView}.
	 * @param ctx the parse tree
	 */
	void exitCreateView(StatementParser.CreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterView}.
	 * @param ctx the parse tree
	 */
	void enterAlterView(StatementParser.AlterViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterView}.
	 * @param ctx the parse tree
	 */
	void exitAlterView(StatementParser.AlterViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropView}.
	 * @param ctx the parse tree
	 */
	void enterDropView(StatementParser.DropViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropView}.
	 * @param ctx the parse tree
	 */
	void exitDropView(StatementParser.DropViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTablespace}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespace(StatementParser.CreateTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTablespace}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespace(StatementParser.CreateTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceInnodb(StatementParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceInnodb(StatementParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterCreateTablespaceNdb(StatementParser.CreateTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitCreateTablespaceNdb(StatementParser.CreateTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespace(StatementParser.AlterTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTablespace}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespace(StatementParser.AlterTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespaceNdb(StatementParser.AlterTablespaceNdbContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTablespaceNdb}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespaceNdb(StatementParser.AlterTablespaceNdbContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void enterAlterTablespaceInnodb(StatementParser.AlterTablespaceInnodbContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterTablespaceInnodb}.
	 * @param ctx the parse tree
	 */
	void exitAlterTablespaceInnodb(StatementParser.AlterTablespaceInnodbContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void enterDropTablespace(StatementParser.DropTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropTablespace}.
	 * @param ctx the parse tree
	 */
	void exitDropTablespace(StatementParser.DropTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateLogfileGroup(StatementParser.CreateLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateLogfileGroup(StatementParser.CreateLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterLogfileGroup(StatementParser.AlterLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterLogfileGroup(StatementParser.AlterLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropLogfileGroup(StatementParser.DropLogfileGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropLogfileGroup(StatementParser.DropLogfileGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void enterCreateTrigger(StatementParser.CreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTrigger}.
	 * @param ctx the parse tree
	 */
	void exitCreateTrigger(StatementParser.CreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void enterDropTrigger(StatementParser.DropTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropTrigger}.
	 * @param ctx the parse tree
	 */
	void exitDropTrigger(StatementParser.DropTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void enterRenameTable(StatementParser.RenameTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#renameTable}.
	 * @param ctx the parse tree
	 */
	void exitRenameTable(StatementParser.RenameTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createDefinitionClause}.
	 * @param ctx the parse tree
	 */
	void enterCreateDefinitionClause(StatementParser.CreateDefinitionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createDefinitionClause}.
	 * @param ctx the parse tree
	 */
	void exitCreateDefinitionClause(StatementParser.CreateDefinitionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void enterColumnDefinition(StatementParser.ColumnDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnDefinition}.
	 * @param ctx the parse tree
	 */
	void exitColumnDefinition(StatementParser.ColumnDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fieldDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFieldDefinition(StatementParser.FieldDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fieldDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFieldDefinition(StatementParser.FieldDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnAttribute}.
	 * @param ctx the parse tree
	 */
	void enterColumnAttribute(StatementParser.ColumnAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnAttribute}.
	 * @param ctx the parse tree
	 */
	void exitColumnAttribute(StatementParser.ColumnAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#checkConstraint}.
	 * @param ctx the parse tree
	 */
	void enterCheckConstraint(StatementParser.CheckConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#checkConstraint}.
	 * @param ctx the parse tree
	 */
	void exitCheckConstraint(StatementParser.CheckConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#constraintEnforcement}.
	 * @param ctx the parse tree
	 */
	void enterConstraintEnforcement(StatementParser.ConstraintEnforcementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#constraintEnforcement}.
	 * @param ctx the parse tree
	 */
	void exitConstraintEnforcement(StatementParser.ConstraintEnforcementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#generatedOption}.
	 * @param ctx the parse tree
	 */
	void enterGeneratedOption(StatementParser.GeneratedOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#generatedOption}.
	 * @param ctx the parse tree
	 */
	void exitGeneratedOption(StatementParser.GeneratedOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDefinition(StatementParser.ReferenceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#referenceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDefinition(StatementParser.ReferenceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#onUpdateDelete}.
	 * @param ctx the parse tree
	 */
	void enterOnUpdateDelete(StatementParser.OnUpdateDeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#onUpdateDelete}.
	 * @param ctx the parse tree
	 */
	void exitOnUpdateDelete(StatementParser.OnUpdateDeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#referenceOption}.
	 * @param ctx the parse tree
	 */
	void enterReferenceOption(StatementParser.ReferenceOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#referenceOption}.
	 * @param ctx the parse tree
	 */
	void exitReferenceOption(StatementParser.ReferenceOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexType}.
	 * @param ctx the parse tree
	 */
	void enterIndexType(StatementParser.IndexTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexType}.
	 * @param ctx the parse tree
	 */
	void exitIndexType(StatementParser.IndexTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexTypeClause}.
	 * @param ctx the parse tree
	 */
	void enterIndexTypeClause(StatementParser.IndexTypeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexTypeClause}.
	 * @param ctx the parse tree
	 */
	void exitIndexTypeClause(StatementParser.IndexTypeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#keyParts}.
	 * @param ctx the parse tree
	 */
	void enterKeyParts(StatementParser.KeyPartsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#keyParts}.
	 * @param ctx the parse tree
	 */
	void exitKeyParts(StatementParser.KeyPartsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#keyPart}.
	 * @param ctx the parse tree
	 */
	void enterKeyPart(StatementParser.KeyPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#keyPart}.
	 * @param ctx the parse tree
	 */
	void exitKeyPart(StatementParser.KeyPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#keyPartWithExpression}.
	 * @param ctx the parse tree
	 */
	void enterKeyPartWithExpression(StatementParser.KeyPartWithExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#keyPartWithExpression}.
	 * @param ctx the parse tree
	 */
	void exitKeyPartWithExpression(StatementParser.KeyPartWithExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#keyListWithExpression}.
	 * @param ctx the parse tree
	 */
	void enterKeyListWithExpression(StatementParser.KeyListWithExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#keyListWithExpression}.
	 * @param ctx the parse tree
	 */
	void exitKeyListWithExpression(StatementParser.KeyListWithExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void enterIndexOption(StatementParser.IndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexOption}.
	 * @param ctx the parse tree
	 */
	void exitIndexOption(StatementParser.IndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#commonIndexOption}.
	 * @param ctx the parse tree
	 */
	void enterCommonIndexOption(StatementParser.CommonIndexOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#commonIndexOption}.
	 * @param ctx the parse tree
	 */
	void exitCommonIndexOption(StatementParser.CommonIndexOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(StatementParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(StatementParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createLikeClause}.
	 * @param ctx the parse tree
	 */
	void enterCreateLikeClause(StatementParser.CreateLikeClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createLikeClause}.
	 * @param ctx the parse tree
	 */
	void exitCreateLikeClause(StatementParser.CreateLikeClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createIndexSpecification}.
	 * @param ctx the parse tree
	 */
	void enterCreateIndexSpecification(StatementParser.CreateIndexSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createIndexSpecification}.
	 * @param ctx the parse tree
	 */
	void exitCreateIndexSpecification(StatementParser.CreateIndexSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTableOptions}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableOptions(StatementParser.CreateTableOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTableOptions}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableOptions(StatementParser.CreateTableOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCreateTableOption(StatementParser.CreateTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCreateTableOption(StatementParser.CreateTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createSRSStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateSRSStatement(StatementParser.CreateSRSStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createSRSStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateSRSStatement(StatementParser.CreateSRSStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropSRSStatement}.
	 * @param ctx the parse tree
	 */
	void enterDropSRSStatement(StatementParser.DropSRSStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropSRSStatement}.
	 * @param ctx the parse tree
	 */
	void exitDropSRSStatement(StatementParser.DropSRSStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#srsAttribute}.
	 * @param ctx the parse tree
	 */
	void enterSrsAttribute(StatementParser.SrsAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#srsAttribute}.
	 * @param ctx the parse tree
	 */
	void exitSrsAttribute(StatementParser.SrsAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#place}.
	 * @param ctx the parse tree
	 */
	void enterPlace(StatementParser.PlaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#place}.
	 * @param ctx the parse tree
	 */
	void exitPlace(StatementParser.PlaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitions(StatementParser.PartitionDefinitionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitions(StatementParser.PartitionDefinitionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinition(StatementParser.PartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinition(StatementParser.PartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionLessThanValue}.
	 * @param ctx the parse tree
	 */
	void enterPartitionLessThanValue(StatementParser.PartitionLessThanValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionLessThanValue}.
	 * @param ctx the parse tree
	 */
	void exitPartitionLessThanValue(StatementParser.PartitionLessThanValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionValueList}.
	 * @param ctx the parse tree
	 */
	void enterPartitionValueList(StatementParser.PartitionValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionValueList}.
	 * @param ctx the parse tree
	 */
	void exitPartitionValueList(StatementParser.PartitionValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionDefinitionOption}.
	 * @param ctx the parse tree
	 */
	void enterPartitionDefinitionOption(StatementParser.PartitionDefinitionOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionDefinitionOption}.
	 * @param ctx the parse tree
	 */
	void exitPartitionDefinitionOption(StatementParser.PartitionDefinitionOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterSubpartitionDefinition(StatementParser.SubpartitionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitSubpartitionDefinition(StatementParser.SubpartitionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void enterOwnerStatement(StatementParser.OwnerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#ownerStatement}.
	 * @param ctx the parse tree
	 */
	void exitOwnerStatement(StatementParser.OwnerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void enterScheduleExpression(StatementParser.ScheduleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#scheduleExpression}.
	 * @param ctx the parse tree
	 */
	void exitScheduleExpression(StatementParser.ScheduleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void enterTimestampValue(StatementParser.TimestampValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#timestampValue}.
	 * @param ctx the parse tree
	 */
	void exitTimestampValue(StatementParser.TimestampValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void enterRoutineBody(StatementParser.RoutineBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#routineBody}.
	 * @param ctx the parse tree
	 */
	void exitRoutineBody(StatementParser.RoutineBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void enterServerOption(StatementParser.ServerOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#serverOption}.
	 * @param ctx the parse tree
	 */
	void exitServerOption(StatementParser.ServerOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void enterRoutineOption(StatementParser.RoutineOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#routineOption}.
	 * @param ctx the parse tree
	 */
	void exitRoutineOption(StatementParser.RoutineOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void enterProcedureParameter(StatementParser.ProcedureParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#procedureParameter}.
	 * @param ctx the parse tree
	 */
	void exitProcedureParameter(StatementParser.ProcedureParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFileSizeLiteral(StatementParser.FileSizeLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFileSizeLiteral(StatementParser.FileSizeLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStatement(StatementParser.SimpleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStatement(StatementParser.SimpleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(StatementParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(StatementParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#validStatement}.
	 * @param ctx the parse tree
	 */
	void enterValidStatement(StatementParser.ValidStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#validStatement}.
	 * @param ctx the parse tree
	 */
	void exitValidStatement(StatementParser.ValidStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#beginStatement}.
	 * @param ctx the parse tree
	 */
	void enterBeginStatement(StatementParser.BeginStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#beginStatement}.
	 * @param ctx the parse tree
	 */
	void exitBeginStatement(StatementParser.BeginStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#declareStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclareStatement(StatementParser.DeclareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#declareStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclareStatement(StatementParser.DeclareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#flowControlStatement}.
	 * @param ctx the parse tree
	 */
	void enterFlowControlStatement(StatementParser.FlowControlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#flowControlStatement}.
	 * @param ctx the parse tree
	 */
	void exitFlowControlStatement(StatementParser.FlowControlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCaseStatement(StatementParser.CaseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#caseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCaseStatement(StatementParser.CaseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(StatementParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(StatementParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterateStatement(StatementParser.IterateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#iterateStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterateStatement(StatementParser.IterateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void enterLeaveStatement(StatementParser.LeaveStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#leaveStatement}.
	 * @param ctx the parse tree
	 */
	void exitLeaveStatement(StatementParser.LeaveStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(StatementParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(StatementParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(StatementParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(StatementParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(StatementParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(StatementParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(StatementParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(StatementParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorStatement(StatementParser.CursorStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorStatement(StatementParser.CursorStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorCloseStatement(StatementParser.CursorCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorCloseStatement(StatementParser.CursorCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorDeclareStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorDeclareStatement(StatementParser.CursorDeclareStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorDeclareStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorDeclareStatement(StatementParser.CursorDeclareStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorFetchStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorFetchStatement(StatementParser.CursorFetchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorFetchStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorFetchStatement(StatementParser.CursorFetchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterCursorOpenStatement(StatementParser.CursorOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitCursorOpenStatement(StatementParser.CursorOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionHandlingStatement}.
	 * @param ctx the parse tree
	 */
	void enterConditionHandlingStatement(StatementParser.ConditionHandlingStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionHandlingStatement}.
	 * @param ctx the parse tree
	 */
	void exitConditionHandlingStatement(StatementParser.ConditionHandlingStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#declareConditionStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclareConditionStatement(StatementParser.DeclareConditionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#declareConditionStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclareConditionStatement(StatementParser.DeclareConditionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#declareHandlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclareHandlerStatement(StatementParser.DeclareHandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#declareHandlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclareHandlerStatement(StatementParser.DeclareHandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#getDiagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void enterGetDiagnosticsStatement(StatementParser.GetDiagnosticsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#getDiagnosticsStatement}.
	 * @param ctx the parse tree
	 */
	void exitGetDiagnosticsStatement(StatementParser.GetDiagnosticsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#statementInformationItem}.
	 * @param ctx the parse tree
	 */
	void enterStatementInformationItem(StatementParser.StatementInformationItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#statementInformationItem}.
	 * @param ctx the parse tree
	 */
	void exitStatementInformationItem(StatementParser.StatementInformationItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionInformationItem}.
	 * @param ctx the parse tree
	 */
	void enterConditionInformationItem(StatementParser.ConditionInformationItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionInformationItem}.
	 * @param ctx the parse tree
	 */
	void exitConditionInformationItem(StatementParser.ConditionInformationItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionNumber}.
	 * @param ctx the parse tree
	 */
	void enterConditionNumber(StatementParser.ConditionNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionNumber}.
	 * @param ctx the parse tree
	 */
	void exitConditionNumber(StatementParser.ConditionNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#statementInformationItemName}.
	 * @param ctx the parse tree
	 */
	void enterStatementInformationItemName(StatementParser.StatementInformationItemNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#statementInformationItemName}.
	 * @param ctx the parse tree
	 */
	void exitStatementInformationItemName(StatementParser.StatementInformationItemNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionInformationItemName}.
	 * @param ctx the parse tree
	 */
	void enterConditionInformationItemName(StatementParser.ConditionInformationItemNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionInformationItemName}.
	 * @param ctx the parse tree
	 */
	void exitConditionInformationItemName(StatementParser.ConditionInformationItemNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerAction}.
	 * @param ctx the parse tree
	 */
	void enterHandlerAction(StatementParser.HandlerActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerAction}.
	 * @param ctx the parse tree
	 */
	void exitHandlerAction(StatementParser.HandlerActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionValue}.
	 * @param ctx the parse tree
	 */
	void enterConditionValue(StatementParser.ConditionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionValue}.
	 * @param ctx the parse tree
	 */
	void exitConditionValue(StatementParser.ConditionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void enterResignalStatement(StatementParser.ResignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#resignalStatement}.
	 * @param ctx the parse tree
	 */
	void exitResignalStatement(StatementParser.ResignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void enterSignalStatement(StatementParser.SignalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#signalStatement}.
	 * @param ctx the parse tree
	 */
	void exitSignalStatement(StatementParser.SignalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#signalInformationItem}.
	 * @param ctx the parse tree
	 */
	void enterSignalInformationItem(StatementParser.SignalInformationItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#signalInformationItem}.
	 * @param ctx the parse tree
	 */
	void exitSignalInformationItem(StatementParser.SignalInformationItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#prepare}.
	 * @param ctx the parse tree
	 */
	void enterPrepare(StatementParser.PrepareContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#prepare}.
	 * @param ctx the parse tree
	 */
	void exitPrepare(StatementParser.PrepareContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#executeStmt}.
	 * @param ctx the parse tree
	 */
	void enterExecuteStmt(StatementParser.ExecuteStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#executeStmt}.
	 * @param ctx the parse tree
	 */
	void exitExecuteStmt(StatementParser.ExecuteStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#executeVarList}.
	 * @param ctx the parse tree
	 */
	void enterExecuteVarList(StatementParser.ExecuteVarListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#executeVarList}.
	 * @param ctx the parse tree
	 */
	void exitExecuteVarList(StatementParser.ExecuteVarListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#deallocate}.
	 * @param ctx the parse tree
	 */
	void enterDeallocate(StatementParser.DeallocateContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#deallocate}.
	 * @param ctx the parse tree
	 */
	void exitDeallocate(StatementParser.DeallocateContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#insert}.
	 * @param ctx the parse tree
	 */
	void enterInsert(StatementParser.InsertContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#insert}.
	 * @param ctx the parse tree
	 */
	void exitInsert(StatementParser.InsertContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#insertSpecification}.
	 * @param ctx the parse tree
	 */
	void enterInsertSpecification(StatementParser.InsertSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#insertSpecification}.
	 * @param ctx the parse tree
	 */
	void exitInsertSpecification(StatementParser.InsertSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#insertValuesClause}.
	 * @param ctx the parse tree
	 */
	void enterInsertValuesClause(StatementParser.InsertValuesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#insertValuesClause}.
	 * @param ctx the parse tree
	 */
	void exitInsertValuesClause(StatementParser.InsertValuesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fields}.
	 * @param ctx the parse tree
	 */
	void enterFields(StatementParser.FieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fields}.
	 * @param ctx the parse tree
	 */
	void exitFields(StatementParser.FieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#insertIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterInsertIdentifier(StatementParser.InsertIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#insertIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitInsertIdentifier(StatementParser.InsertIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableWild}.
	 * @param ctx the parse tree
	 */
	void enterTableWild(StatementParser.TableWildContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableWild}.
	 * @param ctx the parse tree
	 */
	void exitTableWild(StatementParser.TableWildContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#insertSelectClause}.
	 * @param ctx the parse tree
	 */
	void enterInsertSelectClause(StatementParser.InsertSelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#insertSelectClause}.
	 * @param ctx the parse tree
	 */
	void exitInsertSelectClause(StatementParser.InsertSelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#onDuplicateKeyClause}.
	 * @param ctx the parse tree
	 */
	void enterOnDuplicateKeyClause(StatementParser.OnDuplicateKeyClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#onDuplicateKeyClause}.
	 * @param ctx the parse tree
	 */
	void exitOnDuplicateKeyClause(StatementParser.OnDuplicateKeyClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#valueReference}.
	 * @param ctx the parse tree
	 */
	void enterValueReference(StatementParser.ValueReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#valueReference}.
	 * @param ctx the parse tree
	 */
	void exitValueReference(StatementParser.ValueReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#derivedColumns}.
	 * @param ctx the parse tree
	 */
	void enterDerivedColumns(StatementParser.DerivedColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#derivedColumns}.
	 * @param ctx the parse tree
	 */
	void exitDerivedColumns(StatementParser.DerivedColumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#replace}.
	 * @param ctx the parse tree
	 */
	void enterReplace(StatementParser.ReplaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#replace}.
	 * @param ctx the parse tree
	 */
	void exitReplace(StatementParser.ReplaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#replaceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterReplaceSpecification(StatementParser.ReplaceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#replaceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitReplaceSpecification(StatementParser.ReplaceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#replaceValuesClause}.
	 * @param ctx the parse tree
	 */
	void enterReplaceValuesClause(StatementParser.ReplaceValuesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#replaceValuesClause}.
	 * @param ctx the parse tree
	 */
	void exitReplaceValuesClause(StatementParser.ReplaceValuesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#replaceSelectClause}.
	 * @param ctx the parse tree
	 */
	void enterReplaceSelectClause(StatementParser.ReplaceSelectClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#replaceSelectClause}.
	 * @param ctx the parse tree
	 */
	void exitReplaceSelectClause(StatementParser.ReplaceSelectClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#update}.
	 * @param ctx the parse tree
	 */
	void enterUpdate(StatementParser.UpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#update}.
	 * @param ctx the parse tree
	 */
	void exitUpdate(StatementParser.UpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#updateSpecification_}.
	 * @param ctx the parse tree
	 */
	void enterUpdateSpecification_(StatementParser.UpdateSpecification_Context ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#updateSpecification_}.
	 * @param ctx the parse tree
	 */
	void exitUpdateSpecification_(StatementParser.UpdateSpecification_Context ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(StatementParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(StatementParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setAssignmentsClause}.
	 * @param ctx the parse tree
	 */
	void enterSetAssignmentsClause(StatementParser.SetAssignmentsClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setAssignmentsClause}.
	 * @param ctx the parse tree
	 */
	void exitSetAssignmentsClause(StatementParser.SetAssignmentsClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#assignmentValues}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentValues(StatementParser.AssignmentValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#assignmentValues}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentValues(StatementParser.AssignmentValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#assignmentValue}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentValue(StatementParser.AssignmentValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#assignmentValue}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentValue(StatementParser.AssignmentValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#blobValue}.
	 * @param ctx the parse tree
	 */
	void enterBlobValue(StatementParser.BlobValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#blobValue}.
	 * @param ctx the parse tree
	 */
	void exitBlobValue(StatementParser.BlobValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#delete}.
	 * @param ctx the parse tree
	 */
	void enterDelete(StatementParser.DeleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#delete}.
	 * @param ctx the parse tree
	 */
	void exitDelete(StatementParser.DeleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#deleteSpecification}.
	 * @param ctx the parse tree
	 */
	void enterDeleteSpecification(StatementParser.DeleteSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#deleteSpecification}.
	 * @param ctx the parse tree
	 */
	void exitDeleteSpecification(StatementParser.DeleteSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#singleTableClause}.
	 * @param ctx the parse tree
	 */
	void enterSingleTableClause(StatementParser.SingleTableClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#singleTableClause}.
	 * @param ctx the parse tree
	 */
	void exitSingleTableClause(StatementParser.SingleTableClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#multipleTablesClause}.
	 * @param ctx the parse tree
	 */
	void enterMultipleTablesClause(StatementParser.MultipleTablesClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#multipleTablesClause}.
	 * @param ctx the parse tree
	 */
	void exitMultipleTablesClause(StatementParser.MultipleTablesClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(StatementParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(StatementParser.SelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#selectWithInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectWithInto(StatementParser.SelectWithIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#selectWithInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectWithInto(StatementParser.SelectWithIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpression(StatementParser.QueryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#queryExpression}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpression(StatementParser.QueryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#queryExpressionBody}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionBody(StatementParser.QueryExpressionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#queryExpressionBody}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionBody(StatementParser.QueryExpressionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#combineClause}.
	 * @param ctx the parse tree
	 */
	void enterCombineClause(StatementParser.CombineClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#combineClause}.
	 * @param ctx the parse tree
	 */
	void exitCombineClause(StatementParser.CombineClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#queryExpressionParens}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpressionParens(StatementParser.QueryExpressionParensContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#queryExpressionParens}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpressionParens(StatementParser.QueryExpressionParensContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void enterQueryPrimary(StatementParser.QueryPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#queryPrimary}.
	 * @param ctx the parse tree
	 */
	void exitQueryPrimary(StatementParser.QueryPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void enterQuerySpecification(StatementParser.QuerySpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#querySpecification}.
	 * @param ctx the parse tree
	 */
	void exitQuerySpecification(StatementParser.QuerySpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(StatementParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(StatementParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoStatement(StatementParser.DoStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#doStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoStatement(StatementParser.DoStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerStatement(StatementParser.HandlerStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerStatement(StatementParser.HandlerStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerOpenStatement(StatementParser.HandlerOpenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerOpenStatement(StatementParser.HandlerOpenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadIndexStatement(StatementParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadIndexStatement(StatementParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerReadStatement(StatementParser.HandlerReadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerReadStatement(StatementParser.HandlerReadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void enterHandlerCloseStatement(StatementParser.HandlerCloseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 */
	void exitHandlerCloseStatement(StatementParser.HandlerCloseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#importStatement}.
	 * @param ctx the parse tree
	 */
	void enterImportStatement(StatementParser.ImportStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#importStatement}.
	 * @param ctx the parse tree
	 */
	void exitImportStatement(StatementParser.ImportStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loadStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadStatement(StatementParser.LoadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loadStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadStatement(StatementParser.LoadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadDataStatement(StatementParser.LoadDataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loadDataStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadDataStatement(StatementParser.LoadDataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadXmlStatement(StatementParser.LoadXmlStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadXmlStatement(StatementParser.LoadXmlStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableStatement}.
	 * @param ctx the parse tree
	 */
	void enterTableStatement(StatementParser.TableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableStatement}.
	 * @param ctx the parse tree
	 */
	void exitTableStatement(StatementParser.TableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableValueConstructor}.
	 * @param ctx the parse tree
	 */
	void enterTableValueConstructor(StatementParser.TableValueConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableValueConstructor}.
	 * @param ctx the parse tree
	 */
	void exitTableValueConstructor(StatementParser.TableValueConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#rowConstructorList}.
	 * @param ctx the parse tree
	 */
	void enterRowConstructorList(StatementParser.RowConstructorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#rowConstructorList}.
	 * @param ctx the parse tree
	 */
	void exitRowConstructorList(StatementParser.RowConstructorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#withClause}.
	 * @param ctx the parse tree
	 */
	void enterWithClause(StatementParser.WithClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#withClause}.
	 * @param ctx the parse tree
	 */
	void exitWithClause(StatementParser.WithClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cteClause}.
	 * @param ctx the parse tree
	 */
	void enterCteClause(StatementParser.CteClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cteClause}.
	 * @param ctx the parse tree
	 */
	void exitCteClause(StatementParser.CteClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#selectSpecification}.
	 * @param ctx the parse tree
	 */
	void enterSelectSpecification(StatementParser.SelectSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#selectSpecification}.
	 * @param ctx the parse tree
	 */
	void exitSelectSpecification(StatementParser.SelectSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#duplicateSpecification}.
	 * @param ctx the parse tree
	 */
	void enterDuplicateSpecification(StatementParser.DuplicateSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#duplicateSpecification}.
	 * @param ctx the parse tree
	 */
	void exitDuplicateSpecification(StatementParser.DuplicateSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#projections}.
	 * @param ctx the parse tree
	 */
	void enterProjections(StatementParser.ProjectionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#projections}.
	 * @param ctx the parse tree
	 */
	void exitProjections(StatementParser.ProjectionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#projection}.
	 * @param ctx the parse tree
	 */
	void enterProjection(StatementParser.ProjectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#projection}.
	 * @param ctx the parse tree
	 */
	void exitProjection(StatementParser.ProjectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#unqualifiedShorthand}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedShorthand(StatementParser.UnqualifiedShorthandContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#unqualifiedShorthand}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedShorthand(StatementParser.UnqualifiedShorthandContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#qualifiedShorthand}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedShorthand(StatementParser.QualifiedShorthandContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#qualifiedShorthand}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedShorthand(StatementParser.QualifiedShorthandContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void enterFromClause(StatementParser.FromClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fromClause}.
	 * @param ctx the parse tree
	 */
	void exitFromClause(StatementParser.FromClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableReferences}.
	 * @param ctx the parse tree
	 */
	void enterTableReferences(StatementParser.TableReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableReferences}.
	 * @param ctx the parse tree
	 */
	void exitTableReferences(StatementParser.TableReferencesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#escapedTableReference}.
	 * @param ctx the parse tree
	 */
	void enterEscapedTableReference(StatementParser.EscapedTableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#escapedTableReference}.
	 * @param ctx the parse tree
	 */
	void exitEscapedTableReference(StatementParser.EscapedTableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableReference}.
	 * @param ctx the parse tree
	 */
	void enterTableReference(StatementParser.TableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableReference}.
	 * @param ctx the parse tree
	 */
	void exitTableReference(StatementParser.TableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableFactor}.
	 * @param ctx the parse tree
	 */
	void enterTableFactor(StatementParser.TableFactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableFactor}.
	 * @param ctx the parse tree
	 */
	void exitTableFactor(StatementParser.TableFactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionNames}.
	 * @param ctx the parse tree
	 */
	void enterPartitionNames(StatementParser.PartitionNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionNames}.
	 * @param ctx the parse tree
	 */
	void exitPartitionNames(StatementParser.PartitionNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexHintList}.
	 * @param ctx the parse tree
	 */
	void enterIndexHintList(StatementParser.IndexHintListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexHintList}.
	 * @param ctx the parse tree
	 */
	void exitIndexHintList(StatementParser.IndexHintListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void enterIndexHint(StatementParser.IndexHintContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexHint}.
	 * @param ctx the parse tree
	 */
	void exitIndexHint(StatementParser.IndexHintContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#joinedTable}.
	 * @param ctx the parse tree
	 */
	void enterJoinedTable(StatementParser.JoinedTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#joinedTable}.
	 * @param ctx the parse tree
	 */
	void exitJoinedTable(StatementParser.JoinedTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterInnerJoinType(StatementParser.InnerJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#innerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitInnerJoinType(StatementParser.InnerJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void enterOuterJoinType(StatementParser.OuterJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#outerJoinType}.
	 * @param ctx the parse tree
	 */
	void exitOuterJoinType(StatementParser.OuterJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void enterNaturalJoinType(StatementParser.NaturalJoinTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#naturalJoinType}.
	 * @param ctx the parse tree
	 */
	void exitNaturalJoinType(StatementParser.NaturalJoinTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void enterJoinSpecification(StatementParser.JoinSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void exitJoinSpecification(StatementParser.JoinSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(StatementParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(StatementParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupByClause(StatementParser.GroupByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupByClause(StatementParser.GroupByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(StatementParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(StatementParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(StatementParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(StatementParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#limitRowCount}.
	 * @param ctx the parse tree
	 */
	void enterLimitRowCount(StatementParser.LimitRowCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#limitRowCount}.
	 * @param ctx the parse tree
	 */
	void exitLimitRowCount(StatementParser.LimitRowCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#limitOffset}.
	 * @param ctx the parse tree
	 */
	void enterLimitOffset(StatementParser.LimitOffsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#limitOffset}.
	 * @param ctx the parse tree
	 */
	void exitLimitOffset(StatementParser.LimitOffsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#windowClause}.
	 * @param ctx the parse tree
	 */
	void enterWindowClause(StatementParser.WindowClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#windowClause}.
	 * @param ctx the parse tree
	 */
	void exitWindowClause(StatementParser.WindowClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#windowItem}.
	 * @param ctx the parse tree
	 */
	void enterWindowItem(StatementParser.WindowItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#windowItem}.
	 * @param ctx the parse tree
	 */
	void exitWindowItem(StatementParser.WindowItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(StatementParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(StatementParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectLinesInto(StatementParser.SelectLinesIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#selectLinesInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectLinesInto(StatementParser.SelectLinesIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void enterSelectFieldsInto(StatementParser.SelectFieldsIntoContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 */
	void exitSelectFieldsInto(StatementParser.SelectFieldsIntoContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectIntoExpression(StatementParser.SelectIntoExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectIntoExpression(StatementParser.SelectIntoExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void enterLockClause(StatementParser.LockClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lockClause}.
	 * @param ctx the parse tree
	 */
	void exitLockClause(StatementParser.LockClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lockClauseList}.
	 * @param ctx the parse tree
	 */
	void enterLockClauseList(StatementParser.LockClauseListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lockClauseList}.
	 * @param ctx the parse tree
	 */
	void exitLockClauseList(StatementParser.LockClauseListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lockStrength}.
	 * @param ctx the parse tree
	 */
	void enterLockStrength(StatementParser.LockStrengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lockStrength}.
	 * @param ctx the parse tree
	 */
	void exitLockStrength(StatementParser.LockStrengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lockedRowAction}.
	 * @param ctx the parse tree
	 */
	void enterLockedRowAction(StatementParser.LockedRowActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lockedRowAction}.
	 * @param ctx the parse tree
	 */
	void exitLockedRowAction(StatementParser.LockedRowActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableLockingList}.
	 * @param ctx the parse tree
	 */
	void enterTableLockingList(StatementParser.TableLockingListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableLockingList}.
	 * @param ctx the parse tree
	 */
	void exitTableLockingList(StatementParser.TableLockingListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableIdentOptWild}.
	 * @param ctx the parse tree
	 */
	void enterTableIdentOptWild(StatementParser.TableIdentOptWildContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableIdentOptWild}.
	 * @param ctx the parse tree
	 */
	void exitTableIdentOptWild(StatementParser.TableIdentOptWildContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableAliasRefList}.
	 * @param ctx the parse tree
	 */
	void enterTableAliasRefList(StatementParser.TableAliasRefListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableAliasRefList}.
	 * @param ctx the parse tree
	 */
	void exitTableAliasRefList(StatementParser.TableAliasRefListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#parameterMarker}.
	 * @param ctx the parse tree
	 */
	void enterParameterMarker(StatementParser.ParameterMarkerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#parameterMarker}.
	 * @param ctx the parse tree
	 */
	void exitParameterMarker(StatementParser.ParameterMarkerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#customKeyword}.
	 * @param ctx the parse tree
	 */
	void enterCustomKeyword(StatementParser.CustomKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#customKeyword}.
	 * @param ctx the parse tree
	 */
	void exitCustomKeyword(StatementParser.CustomKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#literals}.
	 * @param ctx the parse tree
	 */
	void enterLiterals(StatementParser.LiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#literals}.
	 * @param ctx the parse tree
	 */
	void exitLiterals(StatementParser.LiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#string_}.
	 * @param ctx the parse tree
	 */
	void enterString_(StatementParser.String_Context ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#string_}.
	 * @param ctx the parse tree
	 */
	void exitString_(StatementParser.String_Context ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#stringLiterals}.
	 * @param ctx the parse tree
	 */
	void enterStringLiterals(StatementParser.StringLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#stringLiterals}.
	 * @param ctx the parse tree
	 */
	void exitStringLiterals(StatementParser.StringLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#numberLiterals}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiterals(StatementParser.NumberLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#numberLiterals}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiterals(StatementParser.NumberLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#temporalLiterals}.
	 * @param ctx the parse tree
	 */
	void enterTemporalLiterals(StatementParser.TemporalLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#temporalLiterals}.
	 * @param ctx the parse tree
	 */
	void exitTemporalLiterals(StatementParser.TemporalLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#hexadecimalLiterals}.
	 * @param ctx the parse tree
	 */
	void enterHexadecimalLiterals(StatementParser.HexadecimalLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#hexadecimalLiterals}.
	 * @param ctx the parse tree
	 */
	void exitHexadecimalLiterals(StatementParser.HexadecimalLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#bitValueLiterals}.
	 * @param ctx the parse tree
	 */
	void enterBitValueLiterals(StatementParser.BitValueLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#bitValueLiterals}.
	 * @param ctx the parse tree
	 */
	void exitBitValueLiterals(StatementParser.BitValueLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#booleanLiterals}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiterals(StatementParser.BooleanLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#booleanLiterals}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiterals(StatementParser.BooleanLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#nullValueLiterals}.
	 * @param ctx the parse tree
	 */
	void enterNullValueLiterals(StatementParser.NullValueLiteralsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#nullValueLiterals}.
	 * @param ctx the parse tree
	 */
	void exitNullValueLiterals(StatementParser.NullValueLiteralsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#collationName}.
	 * @param ctx the parse tree
	 */
	void enterCollationName(StatementParser.CollationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#collationName}.
	 * @param ctx the parse tree
	 */
	void exitCollationName(StatementParser.CollationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(StatementParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(StatementParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierKeywordsUnambiguous}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierKeywordsUnambiguous(StatementParser.IdentifierKeywordsUnambiguousContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierKeywordsUnambiguous}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierKeywordsUnambiguous(StatementParser.IdentifierKeywordsUnambiguousContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous1RolesAndLabels}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierKeywordsAmbiguous1RolesAndLabels(StatementParser.IdentifierKeywordsAmbiguous1RolesAndLabelsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous1RolesAndLabels}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierKeywordsAmbiguous1RolesAndLabels(StatementParser.IdentifierKeywordsAmbiguous1RolesAndLabelsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous2Labels}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierKeywordsAmbiguous2Labels(StatementParser.IdentifierKeywordsAmbiguous2LabelsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous2Labels}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierKeywordsAmbiguous2Labels(StatementParser.IdentifierKeywordsAmbiguous2LabelsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous3Roles}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierKeywordsAmbiguous3Roles(StatementParser.IdentifierKeywordsAmbiguous3RolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous3Roles}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierKeywordsAmbiguous3Roles(StatementParser.IdentifierKeywordsAmbiguous3RolesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous4SystemVariables}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierKeywordsAmbiguous4SystemVariables(StatementParser.IdentifierKeywordsAmbiguous4SystemVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous4SystemVariables}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierKeywordsAmbiguous4SystemVariables(StatementParser.IdentifierKeywordsAmbiguous4SystemVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#textOrIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTextOrIdentifier(StatementParser.TextOrIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#textOrIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTextOrIdentifier(StatementParser.TextOrIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(StatementParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(StatementParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userVariable}.
	 * @param ctx the parse tree
	 */
	void enterUserVariable(StatementParser.UserVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userVariable}.
	 * @param ctx the parse tree
	 */
	void exitUserVariable(StatementParser.UserVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#systemVariable}.
	 * @param ctx the parse tree
	 */
	void enterSystemVariable(StatementParser.SystemVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#systemVariable}.
	 * @param ctx the parse tree
	 */
	void exitSystemVariable(StatementParser.SystemVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#rvalueSystemVariable}.
	 * @param ctx the parse tree
	 */
	void enterRvalueSystemVariable(StatementParser.RvalueSystemVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#rvalueSystemVariable}.
	 * @param ctx the parse tree
	 */
	void exitRvalueSystemVariable(StatementParser.RvalueSystemVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setSystemVariable}.
	 * @param ctx the parse tree
	 */
	void enterSetSystemVariable(StatementParser.SetSystemVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setSystemVariable}.
	 * @param ctx the parse tree
	 */
	void exitSetSystemVariable(StatementParser.SetSystemVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionType}.
	 * @param ctx the parse tree
	 */
	void enterOptionType(StatementParser.OptionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionType}.
	 * @param ctx the parse tree
	 */
	void exitOptionType(StatementParser.OptionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#internalVariableName}.
	 * @param ctx the parse tree
	 */
	void enterInternalVariableName(StatementParser.InternalVariableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#internalVariableName}.
	 * @param ctx the parse tree
	 */
	void exitInternalVariableName(StatementParser.InternalVariableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setExprOrDefault}.
	 * @param ctx the parse tree
	 */
	void enterSetExprOrDefault(StatementParser.SetExprOrDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setExprOrDefault}.
	 * @param ctx the parse tree
	 */
	void exitSetExprOrDefault(StatementParser.SetExprOrDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#transactionCharacteristics}.
	 * @param ctx the parse tree
	 */
	void enterTransactionCharacteristics(StatementParser.TransactionCharacteristicsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#transactionCharacteristics}.
	 * @param ctx the parse tree
	 */
	void exitTransactionCharacteristics(StatementParser.TransactionCharacteristicsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#isolationLevel}.
	 * @param ctx the parse tree
	 */
	void enterIsolationLevel(StatementParser.IsolationLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#isolationLevel}.
	 * @param ctx the parse tree
	 */
	void exitIsolationLevel(StatementParser.IsolationLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#isolationTypes}.
	 * @param ctx the parse tree
	 */
	void enterIsolationTypes(StatementParser.IsolationTypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#isolationTypes}.
	 * @param ctx the parse tree
	 */
	void exitIsolationTypes(StatementParser.IsolationTypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#transactionAccessMode}.
	 * @param ctx the parse tree
	 */
	void enterTransactionAccessMode(StatementParser.TransactionAccessModeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#transactionAccessMode}.
	 * @param ctx the parse tree
	 */
	void exitTransactionAccessMode(StatementParser.TransactionAccessModeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#schemaName}.
	 * @param ctx the parse tree
	 */
	void enterSchemaName(StatementParser.SchemaNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#schemaName}.
	 * @param ctx the parse tree
	 */
	void exitSchemaName(StatementParser.SchemaNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#schemaNames}.
	 * @param ctx the parse tree
	 */
	void enterSchemaNames(StatementParser.SchemaNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#schemaNames}.
	 * @param ctx the parse tree
	 */
	void exitSchemaNames(StatementParser.SchemaNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void enterCharsetName(StatementParser.CharsetNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#charsetName}.
	 * @param ctx the parse tree
	 */
	void exitCharsetName(StatementParser.CharsetNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#schemaPairs}.
	 * @param ctx the parse tree
	 */
	void enterSchemaPairs(StatementParser.SchemaPairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#schemaPairs}.
	 * @param ctx the parse tree
	 */
	void exitSchemaPairs(StatementParser.SchemaPairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#schemaPair}.
	 * @param ctx the parse tree
	 */
	void enterSchemaPair(StatementParser.SchemaPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#schemaPair}.
	 * @param ctx the parse tree
	 */
	void exitSchemaPair(StatementParser.SchemaPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(StatementParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(StatementParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(StatementParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(StatementParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#indexName}.
	 * @param ctx the parse tree
	 */
	void enterIndexName(StatementParser.IndexNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#indexName}.
	 * @param ctx the parse tree
	 */
	void exitIndexName(StatementParser.IndexNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#constraintName}.
	 * @param ctx the parse tree
	 */
	void enterConstraintName(StatementParser.ConstraintNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#constraintName}.
	 * @param ctx the parse tree
	 */
	void exitConstraintName(StatementParser.ConstraintNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#delimiterName}.
	 * @param ctx the parse tree
	 */
	void enterDelimiterName(StatementParser.DelimiterNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#delimiterName}.
	 * @param ctx the parse tree
	 */
	void exitDelimiterName(StatementParser.DelimiterNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userIdentifierOrText}.
	 * @param ctx the parse tree
	 */
	void enterUserIdentifierOrText(StatementParser.UserIdentifierOrTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userIdentifierOrText}.
	 * @param ctx the parse tree
	 */
	void exitUserIdentifierOrText(StatementParser.UserIdentifierOrTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#username}.
	 * @param ctx the parse tree
	 */
	void enterUsername(StatementParser.UsernameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#username}.
	 * @param ctx the parse tree
	 */
	void exitUsername(StatementParser.UsernameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#eventName}.
	 * @param ctx the parse tree
	 */
	void enterEventName(StatementParser.EventNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#eventName}.
	 * @param ctx the parse tree
	 */
	void exitEventName(StatementParser.EventNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#serverName}.
	 * @param ctx the parse tree
	 */
	void enterServerName(StatementParser.ServerNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#serverName}.
	 * @param ctx the parse tree
	 */
	void exitServerName(StatementParser.ServerNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#wrapperName}.
	 * @param ctx the parse tree
	 */
	void enterWrapperName(StatementParser.WrapperNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#wrapperName}.
	 * @param ctx the parse tree
	 */
	void exitWrapperName(StatementParser.WrapperNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(StatementParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(StatementParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#procedureName}.
	 * @param ctx the parse tree
	 */
	void enterProcedureName(StatementParser.ProcedureNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#procedureName}.
	 * @param ctx the parse tree
	 */
	void exitProcedureName(StatementParser.ProcedureNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#viewName}.
	 * @param ctx the parse tree
	 */
	void enterViewName(StatementParser.ViewNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#viewName}.
	 * @param ctx the parse tree
	 */
	void exitViewName(StatementParser.ViewNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#owner}.
	 * @param ctx the parse tree
	 */
	void enterOwner(StatementParser.OwnerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#owner}.
	 * @param ctx the parse tree
	 */
	void exitOwner(StatementParser.OwnerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(StatementParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(StatementParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(StatementParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(StatementParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableList}.
	 * @param ctx the parse tree
	 */
	void enterTableList(StatementParser.TableListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableList}.
	 * @param ctx the parse tree
	 */
	void exitTableList(StatementParser.TableListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#viewNames}.
	 * @param ctx the parse tree
	 */
	void enterViewNames(StatementParser.ViewNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#viewNames}.
	 * @param ctx the parse tree
	 */
	void exitViewNames(StatementParser.ViewNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnNames}.
	 * @param ctx the parse tree
	 */
	void enterColumnNames(StatementParser.ColumnNamesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnNames}.
	 * @param ctx the parse tree
	 */
	void exitColumnNames(StatementParser.ColumnNamesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#groupName}.
	 * @param ctx the parse tree
	 */
	void enterGroupName(StatementParser.GroupNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#groupName}.
	 * @param ctx the parse tree
	 */
	void exitGroupName(StatementParser.GroupNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#routineName}.
	 * @param ctx the parse tree
	 */
	void enterRoutineName(StatementParser.RoutineNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#routineName}.
	 * @param ctx the parse tree
	 */
	void exitRoutineName(StatementParser.RoutineNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#shardLibraryName}.
	 * @param ctx the parse tree
	 */
	void enterShardLibraryName(StatementParser.ShardLibraryNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#shardLibraryName}.
	 * @param ctx the parse tree
	 */
	void exitShardLibraryName(StatementParser.ShardLibraryNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#componentName}.
	 * @param ctx the parse tree
	 */
	void enterComponentName(StatementParser.ComponentNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#componentName}.
	 * @param ctx the parse tree
	 */
	void exitComponentName(StatementParser.ComponentNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#pluginName}.
	 * @param ctx the parse tree
	 */
	void enterPluginName(StatementParser.PluginNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#pluginName}.
	 * @param ctx the parse tree
	 */
	void exitPluginName(StatementParser.PluginNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#hostname}.
	 * @param ctx the parse tree
	 */
	void enterHostname(StatementParser.HostnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#hostname}.
	 * @param ctx the parse tree
	 */
	void exitHostname(StatementParser.HostnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#port}.
	 * @param ctx the parse tree
	 */
	void enterPort(StatementParser.PortContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#port}.
	 * @param ctx the parse tree
	 */
	void exitPort(StatementParser.PortContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cloneInstance}.
	 * @param ctx the parse tree
	 */
	void enterCloneInstance(StatementParser.CloneInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cloneInstance}.
	 * @param ctx the parse tree
	 */
	void exitCloneInstance(StatementParser.CloneInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cloneDir}.
	 * @param ctx the parse tree
	 */
	void enterCloneDir(StatementParser.CloneDirContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cloneDir}.
	 * @param ctx the parse tree
	 */
	void exitCloneDir(StatementParser.CloneDirContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#channelName}.
	 * @param ctx the parse tree
	 */
	void enterChannelName(StatementParser.ChannelNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#channelName}.
	 * @param ctx the parse tree
	 */
	void exitChannelName(StatementParser.ChannelNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#logName}.
	 * @param ctx the parse tree
	 */
	void enterLogName(StatementParser.LogNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#logName}.
	 * @param ctx the parse tree
	 */
	void exitLogName(StatementParser.LogNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#roleName}.
	 * @param ctx the parse tree
	 */
	void enterRoleName(StatementParser.RoleNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#roleName}.
	 * @param ctx the parse tree
	 */
	void exitRoleName(StatementParser.RoleNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#roleIdentifierOrText}.
	 * @param ctx the parse tree
	 */
	void enterRoleIdentifierOrText(StatementParser.RoleIdentifierOrTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#roleIdentifierOrText}.
	 * @param ctx the parse tree
	 */
	void exitRoleIdentifierOrText(StatementParser.RoleIdentifierOrTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#engineRef}.
	 * @param ctx the parse tree
	 */
	void enterEngineRef(StatementParser.EngineRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#engineRef}.
	 * @param ctx the parse tree
	 */
	void exitEngineRef(StatementParser.EngineRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#triggerName}.
	 * @param ctx the parse tree
	 */
	void enterTriggerName(StatementParser.TriggerNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#triggerName}.
	 * @param ctx the parse tree
	 */
	void exitTriggerName(StatementParser.TriggerNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#triggerTime}.
	 * @param ctx the parse tree
	 */
	void enterTriggerTime(StatementParser.TriggerTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#triggerTime}.
	 * @param ctx the parse tree
	 */
	void exitTriggerTime(StatementParser.TriggerTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableOrTables}.
	 * @param ctx the parse tree
	 */
	void enterTableOrTables(StatementParser.TableOrTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableOrTables}.
	 * @param ctx the parse tree
	 */
	void exitTableOrTables(StatementParser.TableOrTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userOrRole}.
	 * @param ctx the parse tree
	 */
	void enterUserOrRole(StatementParser.UserOrRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userOrRole}.
	 * @param ctx the parse tree
	 */
	void exitUserOrRole(StatementParser.UserOrRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionName}.
	 * @param ctx the parse tree
	 */
	void enterPartitionName(StatementParser.PartitionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionName}.
	 * @param ctx the parse tree
	 */
	void exitPartitionName(StatementParser.PartitionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(StatementParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(StatementParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#allOrPartitionNameList}.
	 * @param ctx the parse tree
	 */
	void enterAllOrPartitionNameList(StatementParser.AllOrPartitionNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#allOrPartitionNameList}.
	 * @param ctx the parse tree
	 */
	void exitAllOrPartitionNameList(StatementParser.AllOrPartitionNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#triggerEvent}.
	 * @param ctx the parse tree
	 */
	void enterTriggerEvent(StatementParser.TriggerEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#triggerEvent}.
	 * @param ctx the parse tree
	 */
	void exitTriggerEvent(StatementParser.TriggerEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#triggerOrder}.
	 * @param ctx the parse tree
	 */
	void enterTriggerOrder(StatementParser.TriggerOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#triggerOrder}.
	 * @param ctx the parse tree
	 */
	void exitTriggerOrder(StatementParser.TriggerOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(StatementParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(StatementParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#andOperator}.
	 * @param ctx the parse tree
	 */
	void enterAndOperator(StatementParser.AndOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#andOperator}.
	 * @param ctx the parse tree
	 */
	void exitAndOperator(StatementParser.AndOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#orOperator}.
	 * @param ctx the parse tree
	 */
	void enterOrOperator(StatementParser.OrOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#orOperator}.
	 * @param ctx the parse tree
	 */
	void exitOrOperator(StatementParser.OrOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#notOperator}.
	 * @param ctx the parse tree
	 */
	void enterNotOperator(StatementParser.NotOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#notOperator}.
	 * @param ctx the parse tree
	 */
	void exitNotOperator(StatementParser.NotOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#booleanPrimary}.
	 * @param ctx the parse tree
	 */
	void enterBooleanPrimary(StatementParser.BooleanPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#booleanPrimary}.
	 * @param ctx the parse tree
	 */
	void exitBooleanPrimary(StatementParser.BooleanPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(StatementParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(StatementParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(StatementParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(StatementParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(StatementParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(StatementParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#bitExpr}.
	 * @param ctx the parse tree
	 */
	void enterBitExpr(StatementParser.BitExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#bitExpr}.
	 * @param ctx the parse tree
	 */
	void exitBitExpr(StatementParser.BitExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#simpleExpr}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpr(StatementParser.SimpleExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#simpleExpr}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpr(StatementParser.SimpleExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(StatementParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(StatementParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#onEmptyError}.
	 * @param ctx the parse tree
	 */
	void enterOnEmptyError(StatementParser.OnEmptyErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#onEmptyError}.
	 * @param ctx the parse tree
	 */
	void exitOnEmptyError(StatementParser.OnEmptyErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnRef}.
	 * @param ctx the parse tree
	 */
	void enterColumnRef(StatementParser.ColumnRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnRef}.
	 * @param ctx the parse tree
	 */
	void exitColumnRef(StatementParser.ColumnRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnRefList}.
	 * @param ctx the parse tree
	 */
	void enterColumnRefList(StatementParser.ColumnRefListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnRefList}.
	 * @param ctx the parse tree
	 */
	void exitColumnRefList(StatementParser.ColumnRefListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(StatementParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(StatementParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#aggregationFunction}.
	 * @param ctx the parse tree
	 */
	void enterAggregationFunction(StatementParser.AggregationFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#aggregationFunction}.
	 * @param ctx the parse tree
	 */
	void exitAggregationFunction(StatementParser.AggregationFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#jsonFunction}.
	 * @param ctx the parse tree
	 */
	void enterJsonFunction(StatementParser.JsonFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#jsonFunction}.
	 * @param ctx the parse tree
	 */
	void exitJsonFunction(StatementParser.JsonFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#jsonFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterJsonFunctionName(StatementParser.JsonFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#jsonFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitJsonFunctionName(StatementParser.JsonFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterAggregationFunctionName(StatementParser.AggregationFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitAggregationFunctionName(StatementParser.AggregationFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#distinct}.
	 * @param ctx the parse tree
	 */
	void enterDistinct(StatementParser.DistinctContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#distinct}.
	 * @param ctx the parse tree
	 */
	void exitDistinct(StatementParser.DistinctContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#overClause}.
	 * @param ctx the parse tree
	 */
	void enterOverClause(StatementParser.OverClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#overClause}.
	 * @param ctx the parse tree
	 */
	void exitOverClause(StatementParser.OverClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#windowSpecification}.
	 * @param ctx the parse tree
	 */
	void enterWindowSpecification(StatementParser.WindowSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#windowSpecification}.
	 * @param ctx the parse tree
	 */
	void exitWindowSpecification(StatementParser.WindowSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#frameClause}.
	 * @param ctx the parse tree
	 */
	void enterFrameClause(StatementParser.FrameClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#frameClause}.
	 * @param ctx the parse tree
	 */
	void exitFrameClause(StatementParser.FrameClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#frameStart}.
	 * @param ctx the parse tree
	 */
	void enterFrameStart(StatementParser.FrameStartContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#frameStart}.
	 * @param ctx the parse tree
	 */
	void exitFrameStart(StatementParser.FrameStartContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#frameEnd}.
	 * @param ctx the parse tree
	 */
	void enterFrameEnd(StatementParser.FrameEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#frameEnd}.
	 * @param ctx the parse tree
	 */
	void exitFrameEnd(StatementParser.FrameEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#frameBetween}.
	 * @param ctx the parse tree
	 */
	void enterFrameBetween(StatementParser.FrameBetweenContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#frameBetween}.
	 * @param ctx the parse tree
	 */
	void exitFrameBetween(StatementParser.FrameBetweenContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#specialFunction}.
	 * @param ctx the parse tree
	 */
	void enterSpecialFunction(StatementParser.SpecialFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#specialFunction}.
	 * @param ctx the parse tree
	 */
	void exitSpecialFunction(StatementParser.SpecialFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#currentUserFunction}.
	 * @param ctx the parse tree
	 */
	void enterCurrentUserFunction(StatementParser.CurrentUserFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#currentUserFunction}.
	 * @param ctx the parse tree
	 */
	void exitCurrentUserFunction(StatementParser.CurrentUserFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#groupConcatFunction}.
	 * @param ctx the parse tree
	 */
	void enterGroupConcatFunction(StatementParser.GroupConcatFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#groupConcatFunction}.
	 * @param ctx the parse tree
	 */
	void exitGroupConcatFunction(StatementParser.GroupConcatFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void enterWindowFunction(StatementParser.WindowFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#windowFunction}.
	 * @param ctx the parse tree
	 */
	void exitWindowFunction(StatementParser.WindowFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#windowingClause}.
	 * @param ctx the parse tree
	 */
	void enterWindowingClause(StatementParser.WindowingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#windowingClause}.
	 * @param ctx the parse tree
	 */
	void exitWindowingClause(StatementParser.WindowingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#leadLagInfo}.
	 * @param ctx the parse tree
	 */
	void enterLeadLagInfo(StatementParser.LeadLagInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#leadLagInfo}.
	 * @param ctx the parse tree
	 */
	void exitLeadLagInfo(StatementParser.LeadLagInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void enterNullTreatment(StatementParser.NullTreatmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#nullTreatment}.
	 * @param ctx the parse tree
	 */
	void exitNullTreatment(StatementParser.NullTreatmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#checkType}.
	 * @param ctx the parse tree
	 */
	void enterCheckType(StatementParser.CheckTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#checkType}.
	 * @param ctx the parse tree
	 */
	void exitCheckType(StatementParser.CheckTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#repairType}.
	 * @param ctx the parse tree
	 */
	void enterRepairType(StatementParser.RepairTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#repairType}.
	 * @param ctx the parse tree
	 */
	void exitRepairType(StatementParser.RepairTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#castFunction}.
	 * @param ctx the parse tree
	 */
	void enterCastFunction(StatementParser.CastFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#castFunction}.
	 * @param ctx the parse tree
	 */
	void exitCastFunction(StatementParser.CastFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#convertFunction}.
	 * @param ctx the parse tree
	 */
	void enterConvertFunction(StatementParser.ConvertFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#convertFunction}.
	 * @param ctx the parse tree
	 */
	void exitConvertFunction(StatementParser.ConvertFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#castType}.
	 * @param ctx the parse tree
	 */
	void enterCastType(StatementParser.CastTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#castType}.
	 * @param ctx the parse tree
	 */
	void exitCastType(StatementParser.CastTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#nchar}.
	 * @param ctx the parse tree
	 */
	void enterNchar(StatementParser.NcharContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#nchar}.
	 * @param ctx the parse tree
	 */
	void exitNchar(StatementParser.NcharContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#positionFunction}.
	 * @param ctx the parse tree
	 */
	void enterPositionFunction(StatementParser.PositionFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#positionFunction}.
	 * @param ctx the parse tree
	 */
	void exitPositionFunction(StatementParser.PositionFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#substringFunction}.
	 * @param ctx the parse tree
	 */
	void enterSubstringFunction(StatementParser.SubstringFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#substringFunction}.
	 * @param ctx the parse tree
	 */
	void exitSubstringFunction(StatementParser.SubstringFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#extractFunction}.
	 * @param ctx the parse tree
	 */
	void enterExtractFunction(StatementParser.ExtractFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#extractFunction}.
	 * @param ctx the parse tree
	 */
	void exitExtractFunction(StatementParser.ExtractFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#charFunction}.
	 * @param ctx the parse tree
	 */
	void enterCharFunction(StatementParser.CharFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#charFunction}.
	 * @param ctx the parse tree
	 */
	void exitCharFunction(StatementParser.CharFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#trimFunction}.
	 * @param ctx the parse tree
	 */
	void enterTrimFunction(StatementParser.TrimFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#trimFunction}.
	 * @param ctx the parse tree
	 */
	void exitTrimFunction(StatementParser.TrimFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#valuesFunction}.
	 * @param ctx the parse tree
	 */
	void enterValuesFunction(StatementParser.ValuesFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#valuesFunction}.
	 * @param ctx the parse tree
	 */
	void exitValuesFunction(StatementParser.ValuesFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#weightStringFunction}.
	 * @param ctx the parse tree
	 */
	void enterWeightStringFunction(StatementParser.WeightStringFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#weightStringFunction}.
	 * @param ctx the parse tree
	 */
	void exitWeightStringFunction(StatementParser.WeightStringFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#levelClause}.
	 * @param ctx the parse tree
	 */
	void enterLevelClause(StatementParser.LevelClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#levelClause}.
	 * @param ctx the parse tree
	 */
	void exitLevelClause(StatementParser.LevelClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void enterLevelInWeightListElement(StatementParser.LevelInWeightListElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 */
	void exitLevelInWeightListElement(StatementParser.LevelInWeightListElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#regularFunction}.
	 * @param ctx the parse tree
	 */
	void enterRegularFunction(StatementParser.RegularFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#regularFunction}.
	 * @param ctx the parse tree
	 */
	void exitRegularFunction(StatementParser.RegularFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#shorthandRegularFunction}.
	 * @param ctx the parse tree
	 */
	void enterShorthandRegularFunction(StatementParser.ShorthandRegularFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#shorthandRegularFunction}.
	 * @param ctx the parse tree
	 */
	void exitShorthandRegularFunction(StatementParser.ShorthandRegularFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#completeRegularFunction}.
	 * @param ctx the parse tree
	 */
	void enterCompleteRegularFunction(StatementParser.CompleteRegularFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#completeRegularFunction}.
	 * @param ctx the parse tree
	 */
	void exitCompleteRegularFunction(StatementParser.CompleteRegularFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#regularFunctionName}.
	 * @param ctx the parse tree
	 */
	void enterRegularFunctionName(StatementParser.RegularFunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#regularFunctionName}.
	 * @param ctx the parse tree
	 */
	void exitRegularFunctionName(StatementParser.RegularFunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#matchExpression}.
	 * @param ctx the parse tree
	 */
	void enterMatchExpression(StatementParser.MatchExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#matchExpression}.
	 * @param ctx the parse tree
	 */
	void exitMatchExpression(StatementParser.MatchExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#matchSearchModifier}.
	 * @param ctx the parse tree
	 */
	void enterMatchSearchModifier(StatementParser.MatchSearchModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#matchSearchModifier}.
	 * @param ctx the parse tree
	 */
	void exitMatchSearchModifier(StatementParser.MatchSearchModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaseExpression(StatementParser.CaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaseExpression(StatementParser.CaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#datetimeExpr}.
	 * @param ctx the parse tree
	 */
	void enterDatetimeExpr(StatementParser.DatetimeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#datetimeExpr}.
	 * @param ctx the parse tree
	 */
	void exitDatetimeExpr(StatementParser.DatetimeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#binaryLogFileIndexNumber}.
	 * @param ctx the parse tree
	 */
	void enterBinaryLogFileIndexNumber(StatementParser.BinaryLogFileIndexNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#binaryLogFileIndexNumber}.
	 * @param ctx the parse tree
	 */
	void exitBinaryLogFileIndexNumber(StatementParser.BinaryLogFileIndexNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#caseWhen}.
	 * @param ctx the parse tree
	 */
	void enterCaseWhen(StatementParser.CaseWhenContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#caseWhen}.
	 * @param ctx the parse tree
	 */
	void exitCaseWhen(StatementParser.CaseWhenContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#caseElse}.
	 * @param ctx the parse tree
	 */
	void enterCaseElse(StatementParser.CaseElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#caseElse}.
	 * @param ctx the parse tree
	 */
	void exitCaseElse(StatementParser.CaseElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#intervalExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntervalExpression(StatementParser.IntervalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#intervalExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntervalExpression(StatementParser.IntervalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#intervalValue}.
	 * @param ctx the parse tree
	 */
	void enterIntervalValue(StatementParser.IntervalValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#intervalValue}.
	 * @param ctx the parse tree
	 */
	void exitIntervalValue(StatementParser.IntervalValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void enterIntervalUnit(StatementParser.IntervalUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#intervalUnit}.
	 * @param ctx the parse tree
	 */
	void exitIntervalUnit(StatementParser.IntervalUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(StatementParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(StatementParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#orderByItem}.
	 * @param ctx the parse tree
	 */
	void enterOrderByItem(StatementParser.OrderByItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#orderByItem}.
	 * @param ctx the parse tree
	 */
	void exitOrderByItem(StatementParser.OrderByItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(StatementParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(StatementParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#stringList}.
	 * @param ctx the parse tree
	 */
	void enterStringList(StatementParser.StringListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#stringList}.
	 * @param ctx the parse tree
	 */
	void exitStringList(StatementParser.StringListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#textString}.
	 * @param ctx the parse tree
	 */
	void enterTextString(StatementParser.TextStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#textString}.
	 * @param ctx the parse tree
	 */
	void exitTextString(StatementParser.TextStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#textStringHash}.
	 * @param ctx the parse tree
	 */
	void enterTextStringHash(StatementParser.TextStringHashContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#textStringHash}.
	 * @param ctx the parse tree
	 */
	void exitTextStringHash(StatementParser.TextStringHashContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fieldOptions}.
	 * @param ctx the parse tree
	 */
	void enterFieldOptions(StatementParser.FieldOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fieldOptions}.
	 * @param ctx the parse tree
	 */
	void exitFieldOptions(StatementParser.FieldOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#precision}.
	 * @param ctx the parse tree
	 */
	void enterPrecision(StatementParser.PrecisionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#precision}.
	 * @param ctx the parse tree
	 */
	void exitPrecision(StatementParser.PrecisionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#typeDatetimePrecision}.
	 * @param ctx the parse tree
	 */
	void enterTypeDatetimePrecision(StatementParser.TypeDatetimePrecisionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#typeDatetimePrecision}.
	 * @param ctx the parse tree
	 */
	void exitTypeDatetimePrecision(StatementParser.TypeDatetimePrecisionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#charsetWithOptBinary}.
	 * @param ctx the parse tree
	 */
	void enterCharsetWithOptBinary(StatementParser.CharsetWithOptBinaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#charsetWithOptBinary}.
	 * @param ctx the parse tree
	 */
	void exitCharsetWithOptBinary(StatementParser.CharsetWithOptBinaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#ascii}.
	 * @param ctx the parse tree
	 */
	void enterAscii(StatementParser.AsciiContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#ascii}.
	 * @param ctx the parse tree
	 */
	void exitAscii(StatementParser.AsciiContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#unicode}.
	 * @param ctx the parse tree
	 */
	void enterUnicode(StatementParser.UnicodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#unicode}.
	 * @param ctx the parse tree
	 */
	void exitUnicode(StatementParser.UnicodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#charset}.
	 * @param ctx the parse tree
	 */
	void enterCharset(StatementParser.CharsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#charset}.
	 * @param ctx the parse tree
	 */
	void exitCharset(StatementParser.CharsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#defaultCollation}.
	 * @param ctx the parse tree
	 */
	void enterDefaultCollation(StatementParser.DefaultCollationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#defaultCollation}.
	 * @param ctx the parse tree
	 */
	void exitDefaultCollation(StatementParser.DefaultCollationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#defaultEncryption}.
	 * @param ctx the parse tree
	 */
	void enterDefaultEncryption(StatementParser.DefaultEncryptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#defaultEncryption}.
	 * @param ctx the parse tree
	 */
	void exitDefaultEncryption(StatementParser.DefaultEncryptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#defaultCharset}.
	 * @param ctx the parse tree
	 */
	void enterDefaultCharset(StatementParser.DefaultCharsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#defaultCharset}.
	 * @param ctx the parse tree
	 */
	void exitDefaultCharset(StatementParser.DefaultCharsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#now}.
	 * @param ctx the parse tree
	 */
	void enterNow(StatementParser.NowContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#now}.
	 * @param ctx the parse tree
	 */
	void exitNow(StatementParser.NowContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#columnFormat}.
	 * @param ctx the parse tree
	 */
	void enterColumnFormat(StatementParser.ColumnFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#columnFormat}.
	 * @param ctx the parse tree
	 */
	void exitColumnFormat(StatementParser.ColumnFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#storageMedia}.
	 * @param ctx the parse tree
	 */
	void enterStorageMedia(StatementParser.StorageMediaContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#storageMedia}.
	 * @param ctx the parse tree
	 */
	void exitStorageMedia(StatementParser.StorageMediaContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(StatementParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(StatementParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#keyOrIndex}.
	 * @param ctx the parse tree
	 */
	void enterKeyOrIndex(StatementParser.KeyOrIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#keyOrIndex}.
	 * @param ctx the parse tree
	 */
	void exitKeyOrIndex(StatementParser.KeyOrIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fieldLength}.
	 * @param ctx the parse tree
	 */
	void enterFieldLength(StatementParser.FieldLengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fieldLength}.
	 * @param ctx the parse tree
	 */
	void exitFieldLength(StatementParser.FieldLengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#characterSet}.
	 * @param ctx the parse tree
	 */
	void enterCharacterSet(StatementParser.CharacterSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#characterSet}.
	 * @param ctx the parse tree
	 */
	void exitCharacterSet(StatementParser.CharacterSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#collateClause}.
	 * @param ctx the parse tree
	 */
	void enterCollateClause(StatementParser.CollateClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#collateClause}.
	 * @param ctx the parse tree
	 */
	void exitCollateClause(StatementParser.CollateClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fieldOrVarSpec}.
	 * @param ctx the parse tree
	 */
	void enterFieldOrVarSpec(StatementParser.FieldOrVarSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fieldOrVarSpec}.
	 * @param ctx the parse tree
	 */
	void exitFieldOrVarSpec(StatementParser.FieldOrVarSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void enterIfNotExists(StatementParser.IfNotExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#ifNotExists}.
	 * @param ctx the parse tree
	 */
	void exitIfNotExists(StatementParser.IfNotExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void enterIfExists(StatementParser.IfExistsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#ifExists}.
	 * @param ctx the parse tree
	 */
	void exitIfExists(StatementParser.IfExistsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#connectionId}.
	 * @param ctx the parse tree
	 */
	void enterConnectionId(StatementParser.ConnectionIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#connectionId}.
	 * @param ctx the parse tree
	 */
	void exitConnectionId(StatementParser.ConnectionIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#labelName}.
	 * @param ctx the parse tree
	 */
	void enterLabelName(StatementParser.LabelNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#labelName}.
	 * @param ctx the parse tree
	 */
	void exitLabelName(StatementParser.LabelNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cursorName}.
	 * @param ctx the parse tree
	 */
	void enterCursorName(StatementParser.CursorNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cursorName}.
	 * @param ctx the parse tree
	 */
	void exitCursorName(StatementParser.CursorNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#conditionName}.
	 * @param ctx the parse tree
	 */
	void enterConditionName(StatementParser.ConditionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#conditionName}.
	 * @param ctx the parse tree
	 */
	void exitConditionName(StatementParser.ConditionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#combineOption}.
	 * @param ctx the parse tree
	 */
	void enterCombineOption(StatementParser.CombineOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#combineOption}.
	 * @param ctx the parse tree
	 */
	void exitCombineOption(StatementParser.CombineOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#noWriteToBinLog}.
	 * @param ctx the parse tree
	 */
	void enterNoWriteToBinLog(StatementParser.NoWriteToBinLogContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#noWriteToBinLog}.
	 * @param ctx the parse tree
	 */
	void exitNoWriteToBinLog(StatementParser.NoWriteToBinLogContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void enterChannelOption(StatementParser.ChannelOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#channelOption}.
	 * @param ctx the parse tree
	 */
	void exitChannelOption(StatementParser.ChannelOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#use}.
	 * @param ctx the parse tree
	 */
	void enterUse(StatementParser.UseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#use}.
	 * @param ctx the parse tree
	 */
	void exitUse(StatementParser.UseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#help}.
	 * @param ctx the parse tree
	 */
	void enterHelp(StatementParser.HelpContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#help}.
	 * @param ctx the parse tree
	 */
	void exitHelp(StatementParser.HelpContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#explain}.
	 * @param ctx the parse tree
	 */
	void enterExplain(StatementParser.ExplainContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#explain}.
	 * @param ctx the parse tree
	 */
	void exitExplain(StatementParser.ExplainContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fromSchema}.
	 * @param ctx the parse tree
	 */
	void enterFromSchema(StatementParser.FromSchemaContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fromSchema}.
	 * @param ctx the parse tree
	 */
	void exitFromSchema(StatementParser.FromSchemaContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#fromTable}.
	 * @param ctx the parse tree
	 */
	void enterFromTable(StatementParser.FromTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#fromTable}.
	 * @param ctx the parse tree
	 */
	void exitFromTable(StatementParser.FromTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showLike}.
	 * @param ctx the parse tree
	 */
	void enterShowLike(StatementParser.ShowLikeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showLike}.
	 * @param ctx the parse tree
	 */
	void exitShowLike(StatementParser.ShowLikeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showWhereClause}.
	 * @param ctx the parse tree
	 */
	void enterShowWhereClause(StatementParser.ShowWhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showWhereClause}.
	 * @param ctx the parse tree
	 */
	void exitShowWhereClause(StatementParser.ShowWhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void enterShowFilter(StatementParser.ShowFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showFilter}.
	 * @param ctx the parse tree
	 */
	void exitShowFilter(StatementParser.ShowFilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void enterShowProfileType(StatementParser.ShowProfileTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProfileType}.
	 * @param ctx the parse tree
	 */
	void exitShowProfileType(StatementParser.ShowProfileTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setVariable}.
	 * @param ctx the parse tree
	 */
	void enterSetVariable(StatementParser.SetVariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setVariable}.
	 * @param ctx the parse tree
	 */
	void exitSetVariable(StatementParser.SetVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionValueList}.
	 * @param ctx the parse tree
	 */
	void enterOptionValueList(StatementParser.OptionValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionValueList}.
	 * @param ctx the parse tree
	 */
	void exitOptionValueList(StatementParser.OptionValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionValueNoOptionType}.
	 * @param ctx the parse tree
	 */
	void enterOptionValueNoOptionType(StatementParser.OptionValueNoOptionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionValueNoOptionType}.
	 * @param ctx the parse tree
	 */
	void exitOptionValueNoOptionType(StatementParser.OptionValueNoOptionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#equal}.
	 * @param ctx the parse tree
	 */
	void enterEqual(StatementParser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#equal}.
	 * @param ctx the parse tree
	 */
	void exitEqual(StatementParser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionValue}.
	 * @param ctx the parse tree
	 */
	void enterOptionValue(StatementParser.OptionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionValue}.
	 * @param ctx the parse tree
	 */
	void exitOptionValue(StatementParser.OptionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void enterShowBinaryLogs(StatementParser.ShowBinaryLogsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showBinaryLogs}.
	 * @param ctx the parse tree
	 */
	void exitShowBinaryLogs(StatementParser.ShowBinaryLogsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showBinlogEvents}.
	 * @param ctx the parse tree
	 */
	void enterShowBinlogEvents(StatementParser.ShowBinlogEventsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showBinlogEvents}.
	 * @param ctx the parse tree
	 */
	void exitShowBinlogEvents(StatementParser.ShowBinlogEventsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCharacterSet}.
	 * @param ctx the parse tree
	 */
	void enterShowCharacterSet(StatementParser.ShowCharacterSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCharacterSet}.
	 * @param ctx the parse tree
	 */
	void exitShowCharacterSet(StatementParser.ShowCharacterSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCollation}.
	 * @param ctx the parse tree
	 */
	void enterShowCollation(StatementParser.ShowCollationContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCollation}.
	 * @param ctx the parse tree
	 */
	void exitShowCollation(StatementParser.ShowCollationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showColumns}.
	 * @param ctx the parse tree
	 */
	void enterShowColumns(StatementParser.ShowColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showColumns}.
	 * @param ctx the parse tree
	 */
	void exitShowColumns(StatementParser.ShowColumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateDatabase}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateDatabase(StatementParser.ShowCreateDatabaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateDatabase}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateDatabase(StatementParser.ShowCreateDatabaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateEvent}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateEvent(StatementParser.ShowCreateEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateEvent}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateEvent(StatementParser.ShowCreateEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateFunction}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateFunction(StatementParser.ShowCreateFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateFunction}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateFunction(StatementParser.ShowCreateFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateProcedure}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateProcedure(StatementParser.ShowCreateProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateProcedure}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateProcedure(StatementParser.ShowCreateProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateTable}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTable(StatementParser.ShowCreateTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateTable}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTable(StatementParser.ShowCreateTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateTrigger}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateTrigger(StatementParser.ShowCreateTriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateTrigger}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateTrigger(StatementParser.ShowCreateTriggerContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateUser}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateUser(StatementParser.ShowCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateUser}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateUser(StatementParser.ShowCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCreateView}.
	 * @param ctx the parse tree
	 */
	void enterShowCreateView(StatementParser.ShowCreateViewContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCreateView}.
	 * @param ctx the parse tree
	 */
	void exitShowCreateView(StatementParser.ShowCreateViewContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showDatabases}.
	 * @param ctx the parse tree
	 */
	void enterShowDatabases(StatementParser.ShowDatabasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showDatabases}.
	 * @param ctx the parse tree
	 */
	void exitShowDatabases(StatementParser.ShowDatabasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showEngine}.
	 * @param ctx the parse tree
	 */
	void enterShowEngine(StatementParser.ShowEngineContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showEngine}.
	 * @param ctx the parse tree
	 */
	void exitShowEngine(StatementParser.ShowEngineContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showEngines}.
	 * @param ctx the parse tree
	 */
	void enterShowEngines(StatementParser.ShowEnginesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showEngines}.
	 * @param ctx the parse tree
	 */
	void exitShowEngines(StatementParser.ShowEnginesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showErrors}.
	 * @param ctx the parse tree
	 */
	void enterShowErrors(StatementParser.ShowErrorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showErrors}.
	 * @param ctx the parse tree
	 */
	void exitShowErrors(StatementParser.ShowErrorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showEvents}.
	 * @param ctx the parse tree
	 */
	void enterShowEvents(StatementParser.ShowEventsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showEvents}.
	 * @param ctx the parse tree
	 */
	void exitShowEvents(StatementParser.ShowEventsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showFunctionCode}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctionCode(StatementParser.ShowFunctionCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showFunctionCode}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctionCode(StatementParser.ShowFunctionCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showFunctionStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowFunctionStatus(StatementParser.ShowFunctionStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showFunctionStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowFunctionStatus(StatementParser.ShowFunctionStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showGrants}.
	 * @param ctx the parse tree
	 */
	void enterShowGrants(StatementParser.ShowGrantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showGrants}.
	 * @param ctx the parse tree
	 */
	void exitShowGrants(StatementParser.ShowGrantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showIndex}.
	 * @param ctx the parse tree
	 */
	void enterShowIndex(StatementParser.ShowIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showIndex}.
	 * @param ctx the parse tree
	 */
	void exitShowIndex(StatementParser.ShowIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showMasterStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowMasterStatus(StatementParser.ShowMasterStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showMasterStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowMasterStatus(StatementParser.ShowMasterStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showOpenTables}.
	 * @param ctx the parse tree
	 */
	void enterShowOpenTables(StatementParser.ShowOpenTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showOpenTables}.
	 * @param ctx the parse tree
	 */
	void exitShowOpenTables(StatementParser.ShowOpenTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showPlugins}.
	 * @param ctx the parse tree
	 */
	void enterShowPlugins(StatementParser.ShowPluginsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showPlugins}.
	 * @param ctx the parse tree
	 */
	void exitShowPlugins(StatementParser.ShowPluginsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showPrivileges}.
	 * @param ctx the parse tree
	 */
	void enterShowPrivileges(StatementParser.ShowPrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showPrivileges}.
	 * @param ctx the parse tree
	 */
	void exitShowPrivileges(StatementParser.ShowPrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProcedureCode}.
	 * @param ctx the parse tree
	 */
	void enterShowProcedureCode(StatementParser.ShowProcedureCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProcedureCode}.
	 * @param ctx the parse tree
	 */
	void exitShowProcedureCode(StatementParser.ShowProcedureCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProcedureStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowProcedureStatus(StatementParser.ShowProcedureStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProcedureStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowProcedureStatus(StatementParser.ShowProcedureStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProcesslist}.
	 * @param ctx the parse tree
	 */
	void enterShowProcesslist(StatementParser.ShowProcesslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProcesslist}.
	 * @param ctx the parse tree
	 */
	void exitShowProcesslist(StatementParser.ShowProcesslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProfile}.
	 * @param ctx the parse tree
	 */
	void enterShowProfile(StatementParser.ShowProfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProfile}.
	 * @param ctx the parse tree
	 */
	void exitShowProfile(StatementParser.ShowProfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showProfiles}.
	 * @param ctx the parse tree
	 */
	void enterShowProfiles(StatementParser.ShowProfilesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showProfiles}.
	 * @param ctx the parse tree
	 */
	void exitShowProfiles(StatementParser.ShowProfilesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showRelaylogEvent}.
	 * @param ctx the parse tree
	 */
	void enterShowRelaylogEvent(StatementParser.ShowRelaylogEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showRelaylogEvent}.
	 * @param ctx the parse tree
	 */
	void exitShowRelaylogEvent(StatementParser.ShowRelaylogEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showReplicas}.
	 * @param ctx the parse tree
	 */
	void enterShowReplicas(StatementParser.ShowReplicasContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showReplicas}.
	 * @param ctx the parse tree
	 */
	void exitShowReplicas(StatementParser.ShowReplicasContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showSlaveHosts}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveHosts(StatementParser.ShowSlaveHostsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showSlaveHosts}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveHosts(StatementParser.ShowSlaveHostsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showReplicaStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowReplicaStatus(StatementParser.ShowReplicaStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showReplicaStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowReplicaStatus(StatementParser.ShowReplicaStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showSlaveStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowSlaveStatus(StatementParser.ShowSlaveStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showSlaveStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowSlaveStatus(StatementParser.ShowSlaveStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowStatus(StatementParser.ShowStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowStatus(StatementParser.ShowStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showTableStatus}.
	 * @param ctx the parse tree
	 */
	void enterShowTableStatus(StatementParser.ShowTableStatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showTableStatus}.
	 * @param ctx the parse tree
	 */
	void exitShowTableStatus(StatementParser.ShowTableStatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showTables}.
	 * @param ctx the parse tree
	 */
	void enterShowTables(StatementParser.ShowTablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showTables}.
	 * @param ctx the parse tree
	 */
	void exitShowTables(StatementParser.ShowTablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showTriggers}.
	 * @param ctx the parse tree
	 */
	void enterShowTriggers(StatementParser.ShowTriggersContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showTriggers}.
	 * @param ctx the parse tree
	 */
	void exitShowTriggers(StatementParser.ShowTriggersContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showVariables}.
	 * @param ctx the parse tree
	 */
	void enterShowVariables(StatementParser.ShowVariablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showVariables}.
	 * @param ctx the parse tree
	 */
	void exitShowVariables(StatementParser.ShowVariablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showWarnings}.
	 * @param ctx the parse tree
	 */
	void enterShowWarnings(StatementParser.ShowWarningsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showWarnings}.
	 * @param ctx the parse tree
	 */
	void exitShowWarnings(StatementParser.ShowWarningsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#showCharset}.
	 * @param ctx the parse tree
	 */
	void enterShowCharset(StatementParser.ShowCharsetContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#showCharset}.
	 * @param ctx the parse tree
	 */
	void exitShowCharset(StatementParser.ShowCharsetContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setCharacter}.
	 * @param ctx the parse tree
	 */
	void enterSetCharacter(StatementParser.SetCharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setCharacter}.
	 * @param ctx the parse tree
	 */
	void exitSetCharacter(StatementParser.SetCharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#clone}.
	 * @param ctx the parse tree
	 */
	void enterClone(StatementParser.CloneContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#clone}.
	 * @param ctx the parse tree
	 */
	void exitClone(StatementParser.CloneContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cloneAction}.
	 * @param ctx the parse tree
	 */
	void enterCloneAction(StatementParser.CloneActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cloneAction}.
	 * @param ctx the parse tree
	 */
	void exitCloneAction(StatementParser.CloneActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createLoadableFunction}.
	 * @param ctx the parse tree
	 */
	void enterCreateLoadableFunction(StatementParser.CreateLoadableFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createLoadableFunction}.
	 * @param ctx the parse tree
	 */
	void exitCreateLoadableFunction(StatementParser.CreateLoadableFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#install}.
	 * @param ctx the parse tree
	 */
	void enterInstall(StatementParser.InstallContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#install}.
	 * @param ctx the parse tree
	 */
	void exitInstall(StatementParser.InstallContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#uninstall}.
	 * @param ctx the parse tree
	 */
	void enterUninstall(StatementParser.UninstallContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#uninstall}.
	 * @param ctx the parse tree
	 */
	void exitUninstall(StatementParser.UninstallContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#installComponent}.
	 * @param ctx the parse tree
	 */
	void enterInstallComponent(StatementParser.InstallComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#installComponent}.
	 * @param ctx the parse tree
	 */
	void exitInstallComponent(StatementParser.InstallComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void enterInstallPlugin(StatementParser.InstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#installPlugin}.
	 * @param ctx the parse tree
	 */
	void exitInstallPlugin(StatementParser.InstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#uninstallComponent}.
	 * @param ctx the parse tree
	 */
	void enterUninstallComponent(StatementParser.UninstallComponentContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#uninstallComponent}.
	 * @param ctx the parse tree
	 */
	void exitUninstallComponent(StatementParser.UninstallComponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void enterUninstallPlugin(StatementParser.UninstallPluginContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 */
	void exitUninstallPlugin(StatementParser.UninstallPluginContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void enterAnalyzeTable(StatementParser.AnalyzeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#analyzeTable}.
	 * @param ctx the parse tree
	 */
	void exitAnalyzeTable(StatementParser.AnalyzeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#histogram}.
	 * @param ctx the parse tree
	 */
	void enterHistogram(StatementParser.HistogramContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#histogram}.
	 * @param ctx the parse tree
	 */
	void exitHistogram(StatementParser.HistogramContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void enterCheckTable(StatementParser.CheckTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#checkTable}.
	 * @param ctx the parse tree
	 */
	void exitCheckTable(StatementParser.CheckTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void enterCheckTableOption(StatementParser.CheckTableOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#checkTableOption}.
	 * @param ctx the parse tree
	 */
	void exitCheckTableOption(StatementParser.CheckTableOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void enterChecksumTable(StatementParser.ChecksumTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#checksumTable}.
	 * @param ctx the parse tree
	 */
	void exitChecksumTable(StatementParser.ChecksumTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void enterOptimizeTable(StatementParser.OptimizeTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optimizeTable}.
	 * @param ctx the parse tree
	 */
	void exitOptimizeTable(StatementParser.OptimizeTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void enterRepairTable(StatementParser.RepairTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#repairTable}.
	 * @param ctx the parse tree
	 */
	void exitRepairTable(StatementParser.RepairTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterResourceGroup}.
	 * @param ctx the parse tree
	 */
	void enterAlterResourceGroup(StatementParser.AlterResourceGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterResourceGroup}.
	 * @param ctx the parse tree
	 */
	void exitAlterResourceGroup(StatementParser.AlterResourceGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#vcpuSpec}.
	 * @param ctx the parse tree
	 */
	void enterVcpuSpec(StatementParser.VcpuSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#vcpuSpec}.
	 * @param ctx the parse tree
	 */
	void exitVcpuSpec(StatementParser.VcpuSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createResourceGroup}.
	 * @param ctx the parse tree
	 */
	void enterCreateResourceGroup(StatementParser.CreateResourceGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createResourceGroup}.
	 * @param ctx the parse tree
	 */
	void exitCreateResourceGroup(StatementParser.CreateResourceGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropResourceGroup}.
	 * @param ctx the parse tree
	 */
	void enterDropResourceGroup(StatementParser.DropResourceGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropResourceGroup}.
	 * @param ctx the parse tree
	 */
	void exitDropResourceGroup(StatementParser.DropResourceGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setResourceGroup}.
	 * @param ctx the parse tree
	 */
	void enterSetResourceGroup(StatementParser.SetResourceGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setResourceGroup}.
	 * @param ctx the parse tree
	 */
	void exitSetResourceGroup(StatementParser.SetResourceGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#binlog}.
	 * @param ctx the parse tree
	 */
	void enterBinlog(StatementParser.BinlogContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#binlog}.
	 * @param ctx the parse tree
	 */
	void exitBinlog(StatementParser.BinlogContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cacheIndex}.
	 * @param ctx the parse tree
	 */
	void enterCacheIndex(StatementParser.CacheIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cacheIndex}.
	 * @param ctx the parse tree
	 */
	void exitCacheIndex(StatementParser.CacheIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#cacheTableIndexList}.
	 * @param ctx the parse tree
	 */
	void enterCacheTableIndexList(StatementParser.CacheTableIndexListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#cacheTableIndexList}.
	 * @param ctx the parse tree
	 */
	void exitCacheTableIndexList(StatementParser.CacheTableIndexListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#partitionList}.
	 * @param ctx the parse tree
	 */
	void enterPartitionList(StatementParser.PartitionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#partitionList}.
	 * @param ctx the parse tree
	 */
	void exitPartitionList(StatementParser.PartitionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#flush}.
	 * @param ctx the parse tree
	 */
	void enterFlush(StatementParser.FlushContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#flush}.
	 * @param ctx the parse tree
	 */
	void exitFlush(StatementParser.FlushContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void enterFlushOption(StatementParser.FlushOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#flushOption}.
	 * @param ctx the parse tree
	 */
	void exitFlushOption(StatementParser.FlushOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tablesOption}.
	 * @param ctx the parse tree
	 */
	void enterTablesOption(StatementParser.TablesOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tablesOption}.
	 * @param ctx the parse tree
	 */
	void exitTablesOption(StatementParser.TablesOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#kill}.
	 * @param ctx the parse tree
	 */
	void enterKill(StatementParser.KillContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#kill}.
	 * @param ctx the parse tree
	 */
	void exitKill(StatementParser.KillContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loadIndexInfo}.
	 * @param ctx the parse tree
	 */
	void enterLoadIndexInfo(StatementParser.LoadIndexInfoContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loadIndexInfo}.
	 * @param ctx the parse tree
	 */
	void exitLoadIndexInfo(StatementParser.LoadIndexInfoContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#loadTableIndexList}.
	 * @param ctx the parse tree
	 */
	void enterLoadTableIndexList(StatementParser.LoadTableIndexListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#loadTableIndexList}.
	 * @param ctx the parse tree
	 */
	void exitLoadTableIndexList(StatementParser.LoadTableIndexListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void enterResetStatement(StatementParser.ResetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#resetStatement}.
	 * @param ctx the parse tree
	 */
	void exitResetStatement(StatementParser.ResetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#resetOption}.
	 * @param ctx the parse tree
	 */
	void enterResetOption(StatementParser.ResetOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#resetOption}.
	 * @param ctx the parse tree
	 */
	void exitResetOption(StatementParser.ResetOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#resetPersist}.
	 * @param ctx the parse tree
	 */
	void enterResetPersist(StatementParser.ResetPersistContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#resetPersist}.
	 * @param ctx the parse tree
	 */
	void exitResetPersist(StatementParser.ResetPersistContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#restart}.
	 * @param ctx the parse tree
	 */
	void enterRestart(StatementParser.RestartContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#restart}.
	 * @param ctx the parse tree
	 */
	void exitRestart(StatementParser.RestartContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#shutdown}.
	 * @param ctx the parse tree
	 */
	void enterShutdown(StatementParser.ShutdownContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#shutdown}.
	 * @param ctx the parse tree
	 */
	void exitShutdown(StatementParser.ShutdownContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#explainType}.
	 * @param ctx the parse tree
	 */
	void enterExplainType(StatementParser.ExplainTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#explainType}.
	 * @param ctx the parse tree
	 */
	void exitExplainType(StatementParser.ExplainTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#explainableStatement}.
	 * @param ctx the parse tree
	 */
	void enterExplainableStatement(StatementParser.ExplainableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#explainableStatement}.
	 * @param ctx the parse tree
	 */
	void exitExplainableStatement(StatementParser.ExplainableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#formatName}.
	 * @param ctx the parse tree
	 */
	void enterFormatName(StatementParser.FormatNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#formatName}.
	 * @param ctx the parse tree
	 */
	void exitFormatName(StatementParser.FormatNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#delimiter}.
	 * @param ctx the parse tree
	 */
	void enterDelimiter(StatementParser.DelimiterContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#delimiter}.
	 * @param ctx the parse tree
	 */
	void exitDelimiter(StatementParser.DelimiterContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#show}.
	 * @param ctx the parse tree
	 */
	void enterShow(StatementParser.ShowContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#show}.
	 * @param ctx the parse tree
	 */
	void exitShow(StatementParser.ShowContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setTransaction}.
	 * @param ctx the parse tree
	 */
	void enterSetTransaction(StatementParser.SetTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setTransaction}.
	 * @param ctx the parse tree
	 */
	void exitSetTransaction(StatementParser.SetTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setAutoCommit}.
	 * @param ctx the parse tree
	 */
	void enterSetAutoCommit(StatementParser.SetAutoCommitContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setAutoCommit}.
	 * @param ctx the parse tree
	 */
	void exitSetAutoCommit(StatementParser.SetAutoCommitContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#beginTransaction}.
	 * @param ctx the parse tree
	 */
	void enterBeginTransaction(StatementParser.BeginTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#beginTransaction}.
	 * @param ctx the parse tree
	 */
	void exitBeginTransaction(StatementParser.BeginTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#transactionCharacteristic}.
	 * @param ctx the parse tree
	 */
	void enterTransactionCharacteristic(StatementParser.TransactionCharacteristicContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#transactionCharacteristic}.
	 * @param ctx the parse tree
	 */
	void exitTransactionCharacteristic(StatementParser.TransactionCharacteristicContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#commit}.
	 * @param ctx the parse tree
	 */
	void enterCommit(StatementParser.CommitContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#commit}.
	 * @param ctx the parse tree
	 */
	void exitCommit(StatementParser.CommitContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#rollback}.
	 * @param ctx the parse tree
	 */
	void enterRollback(StatementParser.RollbackContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#rollback}.
	 * @param ctx the parse tree
	 */
	void exitRollback(StatementParser.RollbackContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#savepoint}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint(StatementParser.SavepointContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#savepoint}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint(StatementParser.SavepointContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#begin}.
	 * @param ctx the parse tree
	 */
	void enterBegin(StatementParser.BeginContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#begin}.
	 * @param ctx the parse tree
	 */
	void exitBegin(StatementParser.BeginContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lock}.
	 * @param ctx the parse tree
	 */
	void enterLock(StatementParser.LockContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lock}.
	 * @param ctx the parse tree
	 */
	void exitLock(StatementParser.LockContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#unlock}.
	 * @param ctx the parse tree
	 */
	void enterUnlock(StatementParser.UnlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#unlock}.
	 * @param ctx the parse tree
	 */
	void exitUnlock(StatementParser.UnlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#releaseSavepoint}.
	 * @param ctx the parse tree
	 */
	void enterReleaseSavepoint(StatementParser.ReleaseSavepointContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#releaseSavepoint}.
	 * @param ctx the parse tree
	 */
	void exitReleaseSavepoint(StatementParser.ReleaseSavepointContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#xa}.
	 * @param ctx the parse tree
	 */
	void enterXa(StatementParser.XaContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#xa}.
	 * @param ctx the parse tree
	 */
	void exitXa(StatementParser.XaContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionChain}.
	 * @param ctx the parse tree
	 */
	void enterOptionChain(StatementParser.OptionChainContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionChain}.
	 * @param ctx the parse tree
	 */
	void exitOptionChain(StatementParser.OptionChainContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#optionRelease}.
	 * @param ctx the parse tree
	 */
	void enterOptionRelease(StatementParser.OptionReleaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#optionRelease}.
	 * @param ctx the parse tree
	 */
	void exitOptionRelease(StatementParser.OptionReleaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tableLock}.
	 * @param ctx the parse tree
	 */
	void enterTableLock(StatementParser.TableLockContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tableLock}.
	 * @param ctx the parse tree
	 */
	void exitTableLock(StatementParser.TableLockContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#lockOption}.
	 * @param ctx the parse tree
	 */
	void enterLockOption(StatementParser.LockOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#lockOption}.
	 * @param ctx the parse tree
	 */
	void exitLockOption(StatementParser.LockOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#xid}.
	 * @param ctx the parse tree
	 */
	void enterXid(StatementParser.XidContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#xid}.
	 * @param ctx the parse tree
	 */
	void exitXid(StatementParser.XidContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantRoleOrPrivilegeTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void enterGrantRoleOrPrivilegeTo(StatementParser.GrantRoleOrPrivilegeToContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantRoleOrPrivilegeTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void exitGrantRoleOrPrivilegeTo(StatementParser.GrantRoleOrPrivilegeToContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantRoleOrPrivilegeOnTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void enterGrantRoleOrPrivilegeOnTo(StatementParser.GrantRoleOrPrivilegeOnToContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantRoleOrPrivilegeOnTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void exitGrantRoleOrPrivilegeOnTo(StatementParser.GrantRoleOrPrivilegeOnToContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantProxy}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void enterGrantProxy(StatementParser.GrantProxyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantProxy}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 */
	void exitGrantProxy(StatementParser.GrantProxyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 */
	void enterRevokeFrom(StatementParser.RevokeFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 */
	void exitRevokeFrom(StatementParser.RevokeFromContext ctx);
	/**
	 * Enter a parse tree produced by the {@code revokeOnFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 */
	void enterRevokeOnFrom(StatementParser.RevokeOnFromContext ctx);
	/**
	 * Exit a parse tree produced by the {@code revokeOnFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 */
	void exitRevokeOnFrom(StatementParser.RevokeOnFromContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userList}.
	 * @param ctx the parse tree
	 */
	void enterUserList(StatementParser.UserListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userList}.
	 * @param ctx the parse tree
	 */
	void exitUserList(StatementParser.UserListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#roleOrPrivileges}.
	 * @param ctx the parse tree
	 */
	void enterRoleOrPrivileges(StatementParser.RoleOrPrivilegesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#roleOrPrivileges}.
	 * @param ctx the parse tree
	 */
	void exitRoleOrPrivileges(StatementParser.RoleOrPrivilegesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code roleOrDynamicPrivilege}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterRoleOrDynamicPrivilege(StatementParser.RoleOrDynamicPrivilegeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code roleOrDynamicPrivilege}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitRoleOrDynamicPrivilege(StatementParser.RoleOrDynamicPrivilegeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code roleAtHost}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterRoleAtHost(StatementParser.RoleAtHostContext ctx);
	/**
	 * Exit a parse tree produced by the {@code roleAtHost}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitRoleAtHost(StatementParser.RoleAtHostContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeSelect}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeSelect(StatementParser.StaticPrivilegeSelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeSelect}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeSelect(StatementParser.StaticPrivilegeSelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeInsert}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeInsert(StatementParser.StaticPrivilegeInsertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeInsert}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeInsert(StatementParser.StaticPrivilegeInsertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeUpdate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeUpdate(StatementParser.StaticPrivilegeUpdateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeUpdate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeUpdate(StatementParser.StaticPrivilegeUpdateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeReferences}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeReferences(StatementParser.StaticPrivilegeReferencesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeReferences}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeReferences(StatementParser.StaticPrivilegeReferencesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeDelete}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeDelete(StatementParser.StaticPrivilegeDeleteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeDelete}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeDelete(StatementParser.StaticPrivilegeDeleteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeUsage}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeUsage(StatementParser.StaticPrivilegeUsageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeUsage}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeUsage(StatementParser.StaticPrivilegeUsageContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeIndex}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeIndex(StatementParser.StaticPrivilegeIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeIndex}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeIndex(StatementParser.StaticPrivilegeIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeAlter}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeAlter(StatementParser.StaticPrivilegeAlterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeAlter}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeAlter(StatementParser.StaticPrivilegeAlterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreate(StatementParser.StaticPrivilegeCreateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreate(StatementParser.StaticPrivilegeCreateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeDrop}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeDrop(StatementParser.StaticPrivilegeDropContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeDrop}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeDrop(StatementParser.StaticPrivilegeDropContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeExecute}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeExecute(StatementParser.StaticPrivilegeExecuteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeExecute}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeExecute(StatementParser.StaticPrivilegeExecuteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeReload}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeReload(StatementParser.StaticPrivilegeReloadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeReload}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeReload(StatementParser.StaticPrivilegeReloadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeShutdown}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeShutdown(StatementParser.StaticPrivilegeShutdownContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeShutdown}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeShutdown(StatementParser.StaticPrivilegeShutdownContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeProcess}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeProcess(StatementParser.StaticPrivilegeProcessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeProcess}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeProcess(StatementParser.StaticPrivilegeProcessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeFile}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeFile(StatementParser.StaticPrivilegeFileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeFile}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeFile(StatementParser.StaticPrivilegeFileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeGrant}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeGrant(StatementParser.StaticPrivilegeGrantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeGrant}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeGrant(StatementParser.StaticPrivilegeGrantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeShowDatabases}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeShowDatabases(StatementParser.StaticPrivilegeShowDatabasesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeShowDatabases}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeShowDatabases(StatementParser.StaticPrivilegeShowDatabasesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeSuper}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeSuper(StatementParser.StaticPrivilegeSuperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeSuper}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeSuper(StatementParser.StaticPrivilegeSuperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateTemporaryTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateTemporaryTables(StatementParser.StaticPrivilegeCreateTemporaryTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateTemporaryTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateTemporaryTables(StatementParser.StaticPrivilegeCreateTemporaryTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeLockTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeLockTables(StatementParser.StaticPrivilegeLockTablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeLockTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeLockTables(StatementParser.StaticPrivilegeLockTablesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeReplicationSlave}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeReplicationSlave(StatementParser.StaticPrivilegeReplicationSlaveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeReplicationSlave}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeReplicationSlave(StatementParser.StaticPrivilegeReplicationSlaveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeReplicationClient}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeReplicationClient(StatementParser.StaticPrivilegeReplicationClientContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeReplicationClient}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeReplicationClient(StatementParser.StaticPrivilegeReplicationClientContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateView(StatementParser.StaticPrivilegeCreateViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateView(StatementParser.StaticPrivilegeCreateViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeShowView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeShowView(StatementParser.StaticPrivilegeShowViewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeShowView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeShowView(StatementParser.StaticPrivilegeShowViewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateRoutine(StatementParser.StaticPrivilegeCreateRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateRoutine(StatementParser.StaticPrivilegeCreateRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeAlterRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeAlterRoutine(StatementParser.StaticPrivilegeAlterRoutineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeAlterRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeAlterRoutine(StatementParser.StaticPrivilegeAlterRoutineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateUser}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateUser(StatementParser.StaticPrivilegeCreateUserContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateUser}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateUser(StatementParser.StaticPrivilegeCreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeEvent}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeEvent(StatementParser.StaticPrivilegeEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeEvent}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeEvent(StatementParser.StaticPrivilegeEventContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeTrigger}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeTrigger(StatementParser.StaticPrivilegeTriggerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeTrigger}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeTrigger(StatementParser.StaticPrivilegeTriggerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateTablespace}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateTablespace(StatementParser.StaticPrivilegeCreateTablespaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateTablespace}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateTablespace(StatementParser.StaticPrivilegeCreateTablespaceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeCreateRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeCreateRole(StatementParser.StaticPrivilegeCreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeCreateRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeCreateRole(StatementParser.StaticPrivilegeCreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticPrivilegeDropRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void enterStaticPrivilegeDropRole(StatementParser.StaticPrivilegeDropRoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticPrivilegeDropRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 */
	void exitStaticPrivilegeDropRole(StatementParser.StaticPrivilegeDropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#aclType}.
	 * @param ctx the parse tree
	 */
	void enterAclType(StatementParser.AclTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#aclType}.
	 * @param ctx the parse tree
	 */
	void exitAclType(StatementParser.AclTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantLevelGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterGrantLevelGlobal(StatementParser.GrantLevelGlobalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantLevelGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitGrantLevelGlobal(StatementParser.GrantLevelGlobalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantLevelSchemaGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterGrantLevelSchemaGlobal(StatementParser.GrantLevelSchemaGlobalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantLevelSchemaGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitGrantLevelSchemaGlobal(StatementParser.GrantLevelSchemaGlobalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code grantLevelTable}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterGrantLevelTable(StatementParser.GrantLevelTableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code grantLevelTable}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitGrantLevelTable(StatementParser.GrantLevelTableContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createUser}.
	 * @param ctx the parse tree
	 */
	void enterCreateUser(StatementParser.CreateUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createUser}.
	 * @param ctx the parse tree
	 */
	void exitCreateUser(StatementParser.CreateUserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createUserEntryNoOption}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserEntryNoOption(StatementParser.CreateUserEntryNoOptionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createUserEntryNoOption}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserEntryNoOption(StatementParser.CreateUserEntryNoOptionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createUserEntryIdentifiedBy}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserEntryIdentifiedBy(StatementParser.CreateUserEntryIdentifiedByContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createUserEntryIdentifiedBy}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserEntryIdentifiedBy(StatementParser.CreateUserEntryIdentifiedByContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createUserEntryIdentifiedWith}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserEntryIdentifiedWith(StatementParser.CreateUserEntryIdentifiedWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createUserEntryIdentifiedWith}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserEntryIdentifiedWith(StatementParser.CreateUserEntryIdentifiedWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createUserList}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserList(StatementParser.CreateUserListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createUserList}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserList(StatementParser.CreateUserListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#defaultRoleClause}.
	 * @param ctx the parse tree
	 */
	void enterDefaultRoleClause(StatementParser.DefaultRoleClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#defaultRoleClause}.
	 * @param ctx the parse tree
	 */
	void exitDefaultRoleClause(StatementParser.DefaultRoleClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#requireClause}.
	 * @param ctx the parse tree
	 */
	void enterRequireClause(StatementParser.RequireClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#requireClause}.
	 * @param ctx the parse tree
	 */
	void exitRequireClause(StatementParser.RequireClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#connectOptions}.
	 * @param ctx the parse tree
	 */
	void enterConnectOptions(StatementParser.ConnectOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#connectOptions}.
	 * @param ctx the parse tree
	 */
	void exitConnectOptions(StatementParser.ConnectOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#accountLockPasswordExpireOptions}.
	 * @param ctx the parse tree
	 */
	void enterAccountLockPasswordExpireOptions(StatementParser.AccountLockPasswordExpireOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#accountLockPasswordExpireOptions}.
	 * @param ctx the parse tree
	 */
	void exitAccountLockPasswordExpireOptions(StatementParser.AccountLockPasswordExpireOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#accountLockPasswordExpireOption}.
	 * @param ctx the parse tree
	 */
	void enterAccountLockPasswordExpireOption(StatementParser.AccountLockPasswordExpireOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#accountLockPasswordExpireOption}.
	 * @param ctx the parse tree
	 */
	void exitAccountLockPasswordExpireOption(StatementParser.AccountLockPasswordExpireOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void enterAlterUser(StatementParser.AlterUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterUser}.
	 * @param ctx the parse tree
	 */
	void exitAlterUser(StatementParser.AlterUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterUserEntry}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserEntry(StatementParser.AlterUserEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterUserEntry}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserEntry(StatementParser.AlterUserEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#alterUserList}.
	 * @param ctx the parse tree
	 */
	void enterAlterUserList(StatementParser.AlterUserListContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#alterUserList}.
	 * @param ctx the parse tree
	 */
	void exitAlterUserList(StatementParser.AlterUserListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void enterDropUser(StatementParser.DropUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropUser}.
	 * @param ctx the parse tree
	 */
	void exitDropUser(StatementParser.DropUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#createRole}.
	 * @param ctx the parse tree
	 */
	void enterCreateRole(StatementParser.CreateRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#createRole}.
	 * @param ctx the parse tree
	 */
	void exitCreateRole(StatementParser.CreateRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void enterDropRole(StatementParser.DropRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#dropRole}.
	 * @param ctx the parse tree
	 */
	void exitDropRole(StatementParser.DropRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void enterRenameUser(StatementParser.RenameUserContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#renameUser}.
	 * @param ctx the parse tree
	 */
	void exitRenameUser(StatementParser.RenameUserContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setDefaultRole}.
	 * @param ctx the parse tree
	 */
	void enterSetDefaultRole(StatementParser.SetDefaultRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setDefaultRole}.
	 * @param ctx the parse tree
	 */
	void exitSetDefaultRole(StatementParser.SetDefaultRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setRole}.
	 * @param ctx the parse tree
	 */
	void enterSetRole(StatementParser.SetRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setRole}.
	 * @param ctx the parse tree
	 */
	void exitSetRole(StatementParser.SetRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#setPassword}.
	 * @param ctx the parse tree
	 */
	void enterSetPassword(StatementParser.SetPasswordContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#setPassword}.
	 * @param ctx the parse tree
	 */
	void exitSetPassword(StatementParser.SetPasswordContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#authOption}.
	 * @param ctx the parse tree
	 */
	void enterAuthOption(StatementParser.AuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#authOption}.
	 * @param ctx the parse tree
	 */
	void exitAuthOption(StatementParser.AuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#withGrantOption}.
	 * @param ctx the parse tree
	 */
	void enterWithGrantOption(StatementParser.WithGrantOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#withGrantOption}.
	 * @param ctx the parse tree
	 */
	void exitWithGrantOption(StatementParser.WithGrantOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userOrRoles}.
	 * @param ctx the parse tree
	 */
	void enterUserOrRoles(StatementParser.UserOrRolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userOrRoles}.
	 * @param ctx the parse tree
	 */
	void exitUserOrRoles(StatementParser.UserOrRolesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#roles}.
	 * @param ctx the parse tree
	 */
	void enterRoles(StatementParser.RolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#roles}.
	 * @param ctx the parse tree
	 */
	void exitRoles(StatementParser.RolesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#grantAs}.
	 * @param ctx the parse tree
	 */
	void enterGrantAs(StatementParser.GrantAsContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#grantAs}.
	 * @param ctx the parse tree
	 */
	void exitGrantAs(StatementParser.GrantAsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#withRoles}.
	 * @param ctx the parse tree
	 */
	void enterWithRoles(StatementParser.WithRolesContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#withRoles}.
	 * @param ctx the parse tree
	 */
	void exitWithRoles(StatementParser.WithRolesContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterUserAuthOption(StatementParser.UserAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitUserAuthOption(StatementParser.UserAuthOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifiedBy}.
	 * @param ctx the parse tree
	 */
	void enterIdentifiedBy(StatementParser.IdentifiedByContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifiedBy}.
	 * @param ctx the parse tree
	 */
	void exitIdentifiedBy(StatementParser.IdentifiedByContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#identifiedWith}.
	 * @param ctx the parse tree
	 */
	void enterIdentifiedWith(StatementParser.IdentifiedWithContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#identifiedWith}.
	 * @param ctx the parse tree
	 */
	void exitIdentifiedWith(StatementParser.IdentifiedWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#connectOption}.
	 * @param ctx the parse tree
	 */
	void enterConnectOption(StatementParser.ConnectOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#connectOption}.
	 * @param ctx the parse tree
	 */
	void exitConnectOption(StatementParser.ConnectOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void enterTlsOption(StatementParser.TlsOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#tlsOption}.
	 * @param ctx the parse tree
	 */
	void exitTlsOption(StatementParser.TlsOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link StatementParser#userFuncAuthOption}.
	 * @param ctx the parse tree
	 */
	void enterUserFuncAuthOption(StatementParser.UserFuncAuthOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link StatementParser#userFuncAuthOption}.
	 * @param ctx the parse tree
	 */
	void exitUserFuncAuthOption(StatementParser.UserFuncAuthOptionContext ctx);
}