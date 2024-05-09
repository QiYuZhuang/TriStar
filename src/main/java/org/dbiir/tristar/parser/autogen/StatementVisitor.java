package org.dbiir.tristar.parser.autogen;// Generated from /Users/andrew/2024/tristar/src/main/antlr4/Statement.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StatementParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StatementVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StatementParser#execute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute(StatementParser.ExecuteContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterStatement(StatementParser.AlterStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTable(StatementParser.CreateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionClause(StatementParser.PartitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionTypeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionTypeDef(StatementParser.PartitionTypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#subPartitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubPartitions(StatementParser.SubPartitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionKeyAlgorithm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionKeyAlgorithm(StatementParser.PartitionKeyAlgorithmContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#duplicateAsQueryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDuplicateAsQueryExpression(StatementParser.DuplicateAsQueryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(StatementParser.AlterTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#standaloneAlterTableAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandaloneAlterTableAction(StatementParser.StandaloneAlterTableActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTableActions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableActions(StatementParser.AlterTableActionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTablePartitionOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablePartitionOptions(StatementParser.AlterTablePartitionOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterCommandList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCommandList(StatementParser.AlterCommandListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterList(StatementParser.AlterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTableOptionsSpaceSeparated}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableOptionsSpaceSeparated(StatementParser.CreateTableOptionsSpaceSeparatedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddColumn(StatementParser.AddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addTableConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddTableConstraint(StatementParser.AddTableConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code changeColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChangeColumn(StatementParser.ChangeColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modifyColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifyColumn(StatementParser.ModifyColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableDrop}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableDrop(StatementParser.AlterTableDropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code disableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisableKeys(StatementParser.DisableKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code enableKeys}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnableKeys(StatementParser.EnableKeysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterColumn(StatementParser.AlterColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterIndex(StatementParser.AlterIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterCheck}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCheck(StatementParser.AlterCheckContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterConstraint}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterConstraint(StatementParser.AlterConstraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameColumn}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameColumn(StatementParser.RenameColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterRenameTable}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterRenameTable(StatementParser.AlterRenameTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code renameIndex}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameIndex(StatementParser.RenameIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterConvert}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterConvert(StatementParser.AlterConvertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableForce}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableForce(StatementParser.AlterTableForceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterTableOrder}
	 * labeled alternative in {@link StatementParser#alterListItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTableOrder(StatementParser.AlterTableOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterOrderList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterOrderList(StatementParser.AlterOrderListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableConstraintDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableConstraintDef(StatementParser.TableConstraintDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterCommandsModifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCommandsModifierList(StatementParser.AlterCommandsModifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterCommandsModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterCommandsModifier(StatementParser.AlterCommandsModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#withValidation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithValidation(StatementParser.WithValidationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#standaloneAlterCommands}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandaloneAlterCommands(StatementParser.StandaloneAlterCommandsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterPartition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterPartition(StatementParser.AlterPartitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#constraintClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintClause(StatementParser.ConstraintClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableElementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElementList(StatementParser.TableElementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableElement(StatementParser.TableElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#restrict}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestrict(StatementParser.RestrictContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fulltextIndexOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFulltextIndexOption(StatementParser.FulltextIndexOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(StatementParser.DropTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropIndex(StatementParser.DropIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterAlgorithmOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterAlgorithmOption(StatementParser.AlterAlgorithmOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterLockOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterLockOption(StatementParser.AlterLockOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#truncateTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruncateTable(StatementParser.TruncateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndex(StatementParser.CreateIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(StatementParser.CreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabase(StatementParser.AlterDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createDatabaseSpecification_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabaseSpecification_(StatementParser.CreateDatabaseSpecification_Context ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterDatabaseSpecification_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabaseSpecification_(StatementParser.AlterDatabaseSpecification_Context ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(StatementParser.DropDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterInstance(StatementParser.AlterInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#instanceAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceAction(StatementParser.InstanceActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#channel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannel(StatementParser.ChannelContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateEvent(StatementParser.CreateEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterEvent(StatementParser.AlterEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropEvent(StatementParser.DropEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateFunction(StatementParser.CreateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterFunction(StatementParser.AlterFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropFunction(StatementParser.DropFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcedure(StatementParser.CreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterProcedure(StatementParser.AlterProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropProcedure(StatementParser.DropProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateServer(StatementParser.CreateServerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterServer(StatementParser.AlterServerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropServer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropServer(StatementParser.DropServerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateView(StatementParser.CreateViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterView(StatementParser.AlterViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropView(StatementParser.DropViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTablespace(StatementParser.CreateTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTablespaceInnodb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTablespaceInnodb(StatementParser.CreateTablespaceInnodbContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTablespaceNdb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTablespaceNdb(StatementParser.CreateTablespaceNdbContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablespace(StatementParser.AlterTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTablespaceNdb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablespaceNdb(StatementParser.AlterTablespaceNdbContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterTablespaceInnodb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTablespaceInnodb(StatementParser.AlterTablespaceInnodbContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropTablespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTablespace(StatementParser.DropTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateLogfileGroup(StatementParser.CreateLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterLogfileGroup(StatementParser.AlterLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropLogfileGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropLogfileGroup(StatementParser.DropLogfileGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTrigger(StatementParser.CreateTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTrigger(StatementParser.DropTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#renameTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameTable(StatementParser.RenameTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createDefinitionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDefinitionClause(StatementParser.CreateDefinitionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnDefinition(StatementParser.ColumnDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fieldDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDefinition(StatementParser.FieldDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnAttribute(StatementParser.ColumnAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#checkConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckConstraint(StatementParser.CheckConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#constraintEnforcement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintEnforcement(StatementParser.ConstraintEnforcementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#generatedOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeneratedOption(StatementParser.GeneratedOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#referenceDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceDefinition(StatementParser.ReferenceDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#onUpdateDelete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnUpdateDelete(StatementParser.OnUpdateDeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#referenceOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceOption(StatementParser.ReferenceOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexType(StatementParser.IndexTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexTypeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexTypeClause(StatementParser.IndexTypeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#keyParts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyParts(StatementParser.KeyPartsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#keyPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyPart(StatementParser.KeyPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#keyPartWithExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyPartWithExpression(StatementParser.KeyPartWithExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#keyListWithExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyListWithExpression(StatementParser.KeyListWithExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOption(StatementParser.IndexOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#commonIndexOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonIndexOption(StatementParser.CommonIndexOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(StatementParser.VisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createLikeClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateLikeClause(StatementParser.CreateLikeClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createIndexSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateIndexSpecification(StatementParser.CreateIndexSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTableOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableOptions(StatementParser.CreateTableOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createTableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTableOption(StatementParser.CreateTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createSRSStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateSRSStatement(StatementParser.CreateSRSStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropSRSStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropSRSStatement(StatementParser.DropSRSStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#srsAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSrsAttribute(StatementParser.SrsAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#place}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlace(StatementParser.PlaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionDefinitions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinitions(StatementParser.PartitionDefinitionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinition(StatementParser.PartitionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionLessThanValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionLessThanValue(StatementParser.PartitionLessThanValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionValueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionValueList(StatementParser.PartitionValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionDefinitionOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionDefinitionOption(StatementParser.PartitionDefinitionOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#subpartitionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpartitionDefinition(StatementParser.SubpartitionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#ownerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwnerStatement(StatementParser.OwnerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#scheduleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheduleExpression(StatementParser.ScheduleExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#timestampValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestampValue(StatementParser.TimestampValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#routineBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineBody(StatementParser.RoutineBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#serverOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitServerOption(StatementParser.ServerOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#routineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineOption(StatementParser.RoutineOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#procedureParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureParameter(StatementParser.ProcedureParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fileSizeLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileSizeLiteral(StatementParser.FileSizeLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStatement(StatementParser.SimpleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(StatementParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#validStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidStatement(StatementParser.ValidStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#beginStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginStatement(StatementParser.BeginStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#declareStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareStatement(StatementParser.DeclareStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#flowControlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlowControlStatement(StatementParser.FlowControlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#caseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseStatement(StatementParser.CaseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(StatementParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#iterateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterateStatement(StatementParser.IterateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#leaveStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeaveStatement(StatementParser.LeaveStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStatement(StatementParser.LoopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#repeatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(StatementParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(StatementParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(StatementParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorStatement(StatementParser.CursorStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorCloseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorCloseStatement(StatementParser.CursorCloseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorDeclareStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorDeclareStatement(StatementParser.CursorDeclareStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorFetchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorFetchStatement(StatementParser.CursorFetchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorOpenStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorOpenStatement(StatementParser.CursorOpenStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionHandlingStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionHandlingStatement(StatementParser.ConditionHandlingStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#declareConditionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareConditionStatement(StatementParser.DeclareConditionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#declareHandlerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareHandlerStatement(StatementParser.DeclareHandlerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#getDiagnosticsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetDiagnosticsStatement(StatementParser.GetDiagnosticsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#statementInformationItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementInformationItem(StatementParser.StatementInformationItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionInformationItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionInformationItem(StatementParser.ConditionInformationItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionNumber(StatementParser.ConditionNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#statementInformationItemName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementInformationItemName(StatementParser.StatementInformationItemNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionInformationItemName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionInformationItemName(StatementParser.ConditionInformationItemNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerAction(StatementParser.HandlerActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionValue(StatementParser.ConditionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#resignalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResignalStatement(StatementParser.ResignalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#signalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalStatement(StatementParser.SignalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#signalInformationItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalInformationItem(StatementParser.SignalInformationItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#prepare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrepare(StatementParser.PrepareContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#executeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteStmt(StatementParser.ExecuteStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#executeVarList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteVarList(StatementParser.ExecuteVarListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#deallocate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeallocate(StatementParser.DeallocateContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#insert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert(StatementParser.InsertContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#insertSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertSpecification(StatementParser.InsertSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#insertValuesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertValuesClause(StatementParser.InsertValuesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFields(StatementParser.FieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#insertIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertIdentifier(StatementParser.InsertIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableWild}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableWild(StatementParser.TableWildContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#insertSelectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertSelectClause(StatementParser.InsertSelectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#onDuplicateKeyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnDuplicateKeyClause(StatementParser.OnDuplicateKeyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#valueReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueReference(StatementParser.ValueReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#derivedColumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivedColumns(StatementParser.DerivedColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#replace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplace(StatementParser.ReplaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#replaceSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceSpecification(StatementParser.ReplaceSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#replaceValuesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceValuesClause(StatementParser.ReplaceValuesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#replaceSelectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReplaceSelectClause(StatementParser.ReplaceSelectClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(StatementParser.UpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#updateSpecification_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateSpecification_(StatementParser.UpdateSpecification_Context ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(StatementParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setAssignmentsClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAssignmentsClause(StatementParser.SetAssignmentsClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#assignmentValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentValues(StatementParser.AssignmentValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#assignmentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentValue(StatementParser.AssignmentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#blobValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlobValue(StatementParser.BlobValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete(StatementParser.DeleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#deleteSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteSpecification(StatementParser.DeleteSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#singleTableClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleTableClause(StatementParser.SingleTableClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#multipleTablesClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultipleTablesClause(StatementParser.MultipleTablesClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(StatementParser.SelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#selectWithInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectWithInto(StatementParser.SelectWithIntoContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#queryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpression(StatementParser.QueryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#queryExpressionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpressionBody(StatementParser.QueryExpressionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#combineClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCombineClause(StatementParser.CombineClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#queryExpressionParens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpressionParens(StatementParser.QueryExpressionParensContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#queryPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryPrimary(StatementParser.QueryPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#querySpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuerySpecification(StatementParser.QuerySpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(StatementParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#doStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoStatement(StatementParser.DoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerStatement(StatementParser.HandlerStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerOpenStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerOpenStatement(StatementParser.HandlerOpenStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerReadIndexStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerReadIndexStatement(StatementParser.HandlerReadIndexStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerReadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerReadStatement(StatementParser.HandlerReadStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#handlerCloseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerCloseStatement(StatementParser.HandlerCloseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#importStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStatement(StatementParser.ImportStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loadStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadStatement(StatementParser.LoadStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loadDataStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadDataStatement(StatementParser.LoadDataStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loadXmlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadXmlStatement(StatementParser.LoadXmlStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableStatement(StatementParser.TableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableValueConstructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableValueConstructor(StatementParser.TableValueConstructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#rowConstructorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowConstructorList(StatementParser.RowConstructorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#withClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithClause(StatementParser.WithClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cteClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCteClause(StatementParser.CteClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#selectSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectSpecification(StatementParser.SelectSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#duplicateSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDuplicateSpecification(StatementParser.DuplicateSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#projections}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjections(StatementParser.ProjectionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#projection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjection(StatementParser.ProjectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#unqualifiedShorthand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnqualifiedShorthand(StatementParser.UnqualifiedShorthandContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#qualifiedShorthand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedShorthand(StatementParser.QualifiedShorthandContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fromClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromClause(StatementParser.FromClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableReferences}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableReferences(StatementParser.TableReferencesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#escapedTableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedTableReference(StatementParser.EscapedTableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableReference(StatementParser.TableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableFactor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableFactor(StatementParser.TableFactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionNames(StatementParser.PartitionNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexHintList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexHintList(StatementParser.IndexHintListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexHint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexHint(StatementParser.IndexHintContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#joinedTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinedTable(StatementParser.JoinedTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#innerJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerJoinType(StatementParser.InnerJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#outerJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOuterJoinType(StatementParser.OuterJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#naturalJoinType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNaturalJoinType(StatementParser.NaturalJoinTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#joinSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinSpecification(StatementParser.JoinSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(StatementParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(StatementParser.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(StatementParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(StatementParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#limitRowCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitRowCount(StatementParser.LimitRowCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#limitOffset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitOffset(StatementParser.LimitOffsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#windowClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowClause(StatementParser.WindowClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#windowItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowItem(StatementParser.WindowItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(StatementParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#selectLinesInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectLinesInto(StatementParser.SelectLinesIntoContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#selectFieldsInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectFieldsInto(StatementParser.SelectFieldsIntoContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#selectIntoExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectIntoExpression(StatementParser.SelectIntoExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lockClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockClause(StatementParser.LockClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lockClauseList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockClauseList(StatementParser.LockClauseListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lockStrength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockStrength(StatementParser.LockStrengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lockedRowAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockedRowAction(StatementParser.LockedRowActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableLockingList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableLockingList(StatementParser.TableLockingListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableIdentOptWild}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableIdentOptWild(StatementParser.TableIdentOptWildContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableAliasRefList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableAliasRefList(StatementParser.TableAliasRefListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#parameterMarker}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterMarker(StatementParser.ParameterMarkerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#customKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomKeyword(StatementParser.CustomKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#literals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiterals(StatementParser.LiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#string_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_(StatementParser.String_Context ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#stringLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiterals(StatementParser.StringLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#numberLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiterals(StatementParser.NumberLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#temporalLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporalLiterals(StatementParser.TemporalLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#hexadecimalLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexadecimalLiterals(StatementParser.HexadecimalLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#bitValueLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitValueLiterals(StatementParser.BitValueLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#booleanLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiterals(StatementParser.BooleanLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#nullValueLiterals}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValueLiterals(StatementParser.NullValueLiteralsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#collationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollationName(StatementParser.CollationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(StatementParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierKeywordsUnambiguous}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierKeywordsUnambiguous(StatementParser.IdentifierKeywordsUnambiguousContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous1RolesAndLabels}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierKeywordsAmbiguous1RolesAndLabels(StatementParser.IdentifierKeywordsAmbiguous1RolesAndLabelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous2Labels}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierKeywordsAmbiguous2Labels(StatementParser.IdentifierKeywordsAmbiguous2LabelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous3Roles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierKeywordsAmbiguous3Roles(StatementParser.IdentifierKeywordsAmbiguous3RolesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierKeywordsAmbiguous4SystemVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierKeywordsAmbiguous4SystemVariables(StatementParser.IdentifierKeywordsAmbiguous4SystemVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#textOrIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextOrIdentifier(StatementParser.TextOrIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(StatementParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserVariable(StatementParser.UserVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#systemVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemVariable(StatementParser.SystemVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#rvalueSystemVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRvalueSystemVariable(StatementParser.RvalueSystemVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setSystemVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetSystemVariable(StatementParser.SetSystemVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionType(StatementParser.OptionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#internalVariableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInternalVariableName(StatementParser.InternalVariableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setExprOrDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetExprOrDefault(StatementParser.SetExprOrDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#transactionCharacteristics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionCharacteristics(StatementParser.TransactionCharacteristicsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#isolationLevel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsolationLevel(StatementParser.IsolationLevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#isolationTypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsolationTypes(StatementParser.IsolationTypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#transactionAccessMode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionAccessMode(StatementParser.TransactionAccessModeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#schemaName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaName(StatementParser.SchemaNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#schemaNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaNames(StatementParser.SchemaNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#charsetName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharsetName(StatementParser.CharsetNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#schemaPairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaPairs(StatementParser.SchemaPairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#schemaPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaPair(StatementParser.SchemaPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(StatementParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnName(StatementParser.ColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#indexName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexName(StatementParser.IndexNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#constraintName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintName(StatementParser.ConstraintNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#delimiterName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelimiterName(StatementParser.DelimiterNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userIdentifierOrText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserIdentifierOrText(StatementParser.UserIdentifierOrTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#username}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsername(StatementParser.UsernameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#eventName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventName(StatementParser.EventNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#serverName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitServerName(StatementParser.ServerNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#wrapperName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrapperName(StatementParser.WrapperNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(StatementParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#procedureName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureName(StatementParser.ProcedureNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#viewName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewName(StatementParser.ViewNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#owner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOwner(StatementParser.OwnerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(StatementParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(StatementParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableList(StatementParser.TableListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#viewNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewNames(StatementParser.ViewNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnNames}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnNames(StatementParser.ColumnNamesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#groupName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupName(StatementParser.GroupNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#routineName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoutineName(StatementParser.RoutineNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#shardLibraryName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShardLibraryName(StatementParser.ShardLibraryNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#componentName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponentName(StatementParser.ComponentNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#pluginName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPluginName(StatementParser.PluginNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#hostname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHostname(StatementParser.HostnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#port}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort(StatementParser.PortContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cloneInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloneInstance(StatementParser.CloneInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cloneDir}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloneDir(StatementParser.CloneDirContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#channelName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannelName(StatementParser.ChannelNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#logName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogName(StatementParser.LogNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#roleName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleName(StatementParser.RoleNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#roleIdentifierOrText}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleIdentifierOrText(StatementParser.RoleIdentifierOrTextContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#engineRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEngineRef(StatementParser.EngineRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#triggerName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerName(StatementParser.TriggerNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#triggerTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerTime(StatementParser.TriggerTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableOrTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableOrTables(StatementParser.TableOrTablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userOrRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserOrRole(StatementParser.UserOrRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionName(StatementParser.PartitionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(StatementParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#allOrPartitionNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAllOrPartitionNameList(StatementParser.AllOrPartitionNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#triggerEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerEvent(StatementParser.TriggerEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#triggerOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTriggerOrder(StatementParser.TriggerOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(StatementParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#andOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOperator(StatementParser.AndOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#orOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrOperator(StatementParser.OrOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#notOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotOperator(StatementParser.NotOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#booleanPrimary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanPrimary(StatementParser.BooleanPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(StatementParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(StatementParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(StatementParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#bitExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitExpr(StatementParser.BitExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#simpleExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpr(StatementParser.SimpleExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(StatementParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#onEmptyError}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnEmptyError(StatementParser.OnEmptyErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnRef(StatementParser.ColumnRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnRefList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnRefList(StatementParser.ColumnRefListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(StatementParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#aggregationFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregationFunction(StatementParser.AggregationFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#jsonFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonFunction(StatementParser.JsonFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#jsonFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonFunctionName(StatementParser.JsonFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#aggregationFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregationFunctionName(StatementParser.AggregationFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#distinct}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistinct(StatementParser.DistinctContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#overClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOverClause(StatementParser.OverClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#windowSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowSpecification(StatementParser.WindowSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#frameClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameClause(StatementParser.FrameClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#frameStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameStart(StatementParser.FrameStartContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#frameEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameEnd(StatementParser.FrameEndContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#frameBetween}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFrameBetween(StatementParser.FrameBetweenContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#specialFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialFunction(StatementParser.SpecialFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#currentUserFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrentUserFunction(StatementParser.CurrentUserFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#groupConcatFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupConcatFunction(StatementParser.GroupConcatFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#windowFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowFunction(StatementParser.WindowFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#windowingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowingClause(StatementParser.WindowingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#leadLagInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeadLagInfo(StatementParser.LeadLagInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#nullTreatment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullTreatment(StatementParser.NullTreatmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#checkType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckType(StatementParser.CheckTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#repairType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepairType(StatementParser.RepairTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#castFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastFunction(StatementParser.CastFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#convertFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvertFunction(StatementParser.ConvertFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#castType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastType(StatementParser.CastTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#nchar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNchar(StatementParser.NcharContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#positionFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionFunction(StatementParser.PositionFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#substringFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringFunction(StatementParser.SubstringFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#extractFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractFunction(StatementParser.ExtractFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#charFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharFunction(StatementParser.CharFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#trimFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrimFunction(StatementParser.TrimFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#valuesFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesFunction(StatementParser.ValuesFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#weightStringFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWeightStringFunction(StatementParser.WeightStringFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#levelClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevelClause(StatementParser.LevelClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#levelInWeightListElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevelInWeightListElement(StatementParser.LevelInWeightListElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#regularFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularFunction(StatementParser.RegularFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#shorthandRegularFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShorthandRegularFunction(StatementParser.ShorthandRegularFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#completeRegularFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteRegularFunction(StatementParser.CompleteRegularFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#regularFunctionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularFunctionName(StatementParser.RegularFunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#matchExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchExpression(StatementParser.MatchExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#matchSearchModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchSearchModifier(StatementParser.MatchSearchModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#caseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpression(StatementParser.CaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#datetimeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetimeExpr(StatementParser.DatetimeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#binaryLogFileIndexNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryLogFileIndexNumber(StatementParser.BinaryLogFileIndexNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#caseWhen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseWhen(StatementParser.CaseWhenContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#caseElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseElse(StatementParser.CaseElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#intervalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalExpression(StatementParser.IntervalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#intervalValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalValue(StatementParser.IntervalValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#intervalUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntervalUnit(StatementParser.IntervalUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(StatementParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#orderByItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByItem(StatementParser.OrderByItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(StatementParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#stringList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringList(StatementParser.StringListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#textString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextString(StatementParser.TextStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#textStringHash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextStringHash(StatementParser.TextStringHashContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fieldOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldOptions(StatementParser.FieldOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#precision}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecision(StatementParser.PrecisionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#typeDatetimePrecision}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDatetimePrecision(StatementParser.TypeDatetimePrecisionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#charsetWithOptBinary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharsetWithOptBinary(StatementParser.CharsetWithOptBinaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#ascii}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAscii(StatementParser.AsciiContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#unicode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnicode(StatementParser.UnicodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#charset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharset(StatementParser.CharsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#defaultCollation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCollation(StatementParser.DefaultCollationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#defaultEncryption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultEncryption(StatementParser.DefaultEncryptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#defaultCharset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultCharset(StatementParser.DefaultCharsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#now}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNow(StatementParser.NowContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#columnFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnFormat(StatementParser.ColumnFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#storageMedia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageMedia(StatementParser.StorageMediaContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirection(StatementParser.DirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#keyOrIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyOrIndex(StatementParser.KeyOrIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fieldLength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldLength(StatementParser.FieldLengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#characterSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacterSet(StatementParser.CharacterSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#collateClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollateClause(StatementParser.CollateClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fieldOrVarSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldOrVarSpec(StatementParser.FieldOrVarSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#ifNotExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfNotExists(StatementParser.IfNotExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#ifExists}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExists(StatementParser.IfExistsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#connectionId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectionId(StatementParser.ConnectionIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#labelName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelName(StatementParser.LabelNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cursorName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursorName(StatementParser.CursorNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#conditionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionName(StatementParser.ConditionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#combineOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCombineOption(StatementParser.CombineOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#noWriteToBinLog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoWriteToBinLog(StatementParser.NoWriteToBinLogContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#channelOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChannelOption(StatementParser.ChannelOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#use}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse(StatementParser.UseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#help}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHelp(StatementParser.HelpContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#explain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplain(StatementParser.ExplainContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fromSchema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromSchema(StatementParser.FromSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#fromTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromTable(StatementParser.FromTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showLike}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowLike(StatementParser.ShowLikeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showWhereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWhereClause(StatementParser.ShowWhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFilter(StatementParser.ShowFilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProfileType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfileType(StatementParser.ShowProfileTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetVariable(StatementParser.SetVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionValueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionValueList(StatementParser.OptionValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionValueNoOptionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionValueNoOptionType(StatementParser.OptionValueNoOptionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#equal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual(StatementParser.EqualContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionValue(StatementParser.OptionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showBinaryLogs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBinaryLogs(StatementParser.ShowBinaryLogsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showBinlogEvents}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowBinlogEvents(StatementParser.ShowBinlogEventsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCharacterSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCharacterSet(StatementParser.ShowCharacterSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCollation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCollation(StatementParser.ShowCollationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showColumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumns(StatementParser.ShowColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateDatabase(StatementParser.ShowCreateDatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateEvent(StatementParser.ShowCreateEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateFunction(StatementParser.ShowCreateFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateProcedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateProcedure(StatementParser.ShowCreateProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTable(StatementParser.ShowCreateTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateTrigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateTrigger(StatementParser.ShowCreateTriggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateUser(StatementParser.ShowCreateUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCreateView}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCreateView(StatementParser.ShowCreateViewContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showDatabases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabases(StatementParser.ShowDatabasesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showEngine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEngine(StatementParser.ShowEngineContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showEngines}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEngines(StatementParser.ShowEnginesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showErrors}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowErrors(StatementParser.ShowErrorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showEvents}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowEvents(StatementParser.ShowEventsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showFunctionCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctionCode(StatementParser.ShowFunctionCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showFunctionStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowFunctionStatus(StatementParser.ShowFunctionStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showGrants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowGrants(StatementParser.ShowGrantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowIndex(StatementParser.ShowIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showMasterStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowMasterStatus(StatementParser.ShowMasterStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showOpenTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowOpenTables(StatementParser.ShowOpenTablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showPlugins}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPlugins(StatementParser.ShowPluginsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showPrivileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowPrivileges(StatementParser.ShowPrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProcedureCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcedureCode(StatementParser.ShowProcedureCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProcedureStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcedureStatus(StatementParser.ShowProcedureStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProcesslist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProcesslist(StatementParser.ShowProcesslistContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfile(StatementParser.ShowProfileContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showProfiles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowProfiles(StatementParser.ShowProfilesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showRelaylogEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowRelaylogEvent(StatementParser.ShowRelaylogEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showReplicas}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowReplicas(StatementParser.ShowReplicasContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showSlaveHosts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSlaveHosts(StatementParser.ShowSlaveHostsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showReplicaStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowReplicaStatus(StatementParser.ShowReplicaStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showSlaveStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowSlaveStatus(StatementParser.ShowSlaveStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowStatus(StatementParser.ShowStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showTableStatus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTableStatus(StatementParser.ShowTableStatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(StatementParser.ShowTablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showTriggers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTriggers(StatementParser.ShowTriggersContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showVariables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowVariables(StatementParser.ShowVariablesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showWarnings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowWarnings(StatementParser.ShowWarningsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#showCharset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowCharset(StatementParser.ShowCharsetContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setCharacter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetCharacter(StatementParser.SetCharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#clone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClone(StatementParser.CloneContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cloneAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCloneAction(StatementParser.CloneActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createLoadableFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateLoadableFunction(StatementParser.CreateLoadableFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#install}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstall(StatementParser.InstallContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#uninstall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninstall(StatementParser.UninstallContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#installComponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstallComponent(StatementParser.InstallComponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#installPlugin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstallPlugin(StatementParser.InstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#uninstallComponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninstallComponent(StatementParser.UninstallComponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#uninstallPlugin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninstallPlugin(StatementParser.UninstallPluginContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#analyzeTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnalyzeTable(StatementParser.AnalyzeTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#histogram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHistogram(StatementParser.HistogramContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#checkTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckTable(StatementParser.CheckTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#checkTableOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckTableOption(StatementParser.CheckTableOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#checksumTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChecksumTable(StatementParser.ChecksumTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optimizeTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptimizeTable(StatementParser.OptimizeTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#repairTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepairTable(StatementParser.RepairTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterResourceGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterResourceGroup(StatementParser.AlterResourceGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#vcpuSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVcpuSpec(StatementParser.VcpuSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createResourceGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateResourceGroup(StatementParser.CreateResourceGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropResourceGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropResourceGroup(StatementParser.DropResourceGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setResourceGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetResourceGroup(StatementParser.SetResourceGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#binlog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinlog(StatementParser.BinlogContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cacheIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCacheIndex(StatementParser.CacheIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#cacheTableIndexList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCacheTableIndexList(StatementParser.CacheTableIndexListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#partitionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionList(StatementParser.PartitionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#flush}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlush(StatementParser.FlushContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#flushOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlushOption(StatementParser.FlushOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tablesOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablesOption(StatementParser.TablesOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#kill}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKill(StatementParser.KillContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loadIndexInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadIndexInfo(StatementParser.LoadIndexInfoContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#loadTableIndexList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadTableIndexList(StatementParser.LoadTableIndexListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#resetStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetStatement(StatementParser.ResetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#resetOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetOption(StatementParser.ResetOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#resetPersist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResetPersist(StatementParser.ResetPersistContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#restart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestart(StatementParser.RestartContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#shutdown}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShutdown(StatementParser.ShutdownContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#explainType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainType(StatementParser.ExplainTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#explainableStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplainableStatement(StatementParser.ExplainableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#formatName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatName(StatementParser.FormatNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#delimiter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelimiter(StatementParser.DelimiterContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#show}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow(StatementParser.ShowContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetTransaction(StatementParser.SetTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setAutoCommit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAutoCommit(StatementParser.SetAutoCommitContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#beginTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginTransaction(StatementParser.BeginTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#transactionCharacteristic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactionCharacteristic(StatementParser.TransactionCharacteristicContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#commit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommit(StatementParser.CommitContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#rollback}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollback(StatementParser.RollbackContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#savepoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSavepoint(StatementParser.SavepointContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#begin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin(StatementParser.BeginContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLock(StatementParser.LockContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#unlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlock(StatementParser.UnlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#releaseSavepoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReleaseSavepoint(StatementParser.ReleaseSavepointContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#xa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXa(StatementParser.XaContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionChain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionChain(StatementParser.OptionChainContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#optionRelease}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptionRelease(StatementParser.OptionReleaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tableLock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableLock(StatementParser.TableLockContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#lockOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLockOption(StatementParser.LockOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#xid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXid(StatementParser.XidContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantRoleOrPrivilegeTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantRoleOrPrivilegeTo(StatementParser.GrantRoleOrPrivilegeToContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantRoleOrPrivilegeOnTo}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantRoleOrPrivilegeOnTo(StatementParser.GrantRoleOrPrivilegeOnToContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantProxy}
	 * labeled alternative in {@link StatementParser#grant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantProxy(StatementParser.GrantProxyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeFrom(StatementParser.RevokeFromContext ctx);
	/**
	 * Visit a parse tree produced by the {@code revokeOnFrom}
	 * labeled alternative in {@link StatementParser#revoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevokeOnFrom(StatementParser.RevokeOnFromContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserList(StatementParser.UserListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#roleOrPrivileges}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleOrPrivileges(StatementParser.RoleOrPrivilegesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code roleOrDynamicPrivilege}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleOrDynamicPrivilege(StatementParser.RoleOrDynamicPrivilegeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code roleAtHost}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleAtHost(StatementParser.RoleAtHostContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeSelect}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeSelect(StatementParser.StaticPrivilegeSelectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeInsert}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeInsert(StatementParser.StaticPrivilegeInsertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeUpdate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeUpdate(StatementParser.StaticPrivilegeUpdateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeReferences}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeReferences(StatementParser.StaticPrivilegeReferencesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeDelete}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeDelete(StatementParser.StaticPrivilegeDeleteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeUsage}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeUsage(StatementParser.StaticPrivilegeUsageContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeIndex}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeIndex(StatementParser.StaticPrivilegeIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeAlter}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeAlter(StatementParser.StaticPrivilegeAlterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreate}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreate(StatementParser.StaticPrivilegeCreateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeDrop}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeDrop(StatementParser.StaticPrivilegeDropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeExecute}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeExecute(StatementParser.StaticPrivilegeExecuteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeReload}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeReload(StatementParser.StaticPrivilegeReloadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeShutdown}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeShutdown(StatementParser.StaticPrivilegeShutdownContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeProcess}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeProcess(StatementParser.StaticPrivilegeProcessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeFile}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeFile(StatementParser.StaticPrivilegeFileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeGrant}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeGrant(StatementParser.StaticPrivilegeGrantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeShowDatabases}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeShowDatabases(StatementParser.StaticPrivilegeShowDatabasesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeSuper}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeSuper(StatementParser.StaticPrivilegeSuperContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateTemporaryTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateTemporaryTables(StatementParser.StaticPrivilegeCreateTemporaryTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeLockTables}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeLockTables(StatementParser.StaticPrivilegeLockTablesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeReplicationSlave}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeReplicationSlave(StatementParser.StaticPrivilegeReplicationSlaveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeReplicationClient}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeReplicationClient(StatementParser.StaticPrivilegeReplicationClientContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateView(StatementParser.StaticPrivilegeCreateViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeShowView}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeShowView(StatementParser.StaticPrivilegeShowViewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateRoutine(StatementParser.StaticPrivilegeCreateRoutineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeAlterRoutine}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeAlterRoutine(StatementParser.StaticPrivilegeAlterRoutineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateUser}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateUser(StatementParser.StaticPrivilegeCreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeEvent}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeEvent(StatementParser.StaticPrivilegeEventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeTrigger}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeTrigger(StatementParser.StaticPrivilegeTriggerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateTablespace}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateTablespace(StatementParser.StaticPrivilegeCreateTablespaceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeCreateRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeCreateRole(StatementParser.StaticPrivilegeCreateRoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticPrivilegeDropRole}
	 * labeled alternative in {@link StatementParser#roleOrPrivilege}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticPrivilegeDropRole(StatementParser.StaticPrivilegeDropRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#aclType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAclType(StatementParser.AclTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantLevelGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantLevelGlobal(StatementParser.GrantLevelGlobalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantLevelSchemaGlobal}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantLevelSchemaGlobal(StatementParser.GrantLevelSchemaGlobalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code grantLevelTable}
	 * labeled alternative in {@link StatementParser#grantIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantLevelTable(StatementParser.GrantLevelTableContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUser(StatementParser.CreateUserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUserEntryNoOption}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserEntryNoOption(StatementParser.CreateUserEntryNoOptionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUserEntryIdentifiedBy}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserEntryIdentifiedBy(StatementParser.CreateUserEntryIdentifiedByContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createUserEntryIdentifiedWith}
	 * labeled alternative in {@link StatementParser#createUserEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserEntryIdentifiedWith(StatementParser.CreateUserEntryIdentifiedWithContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createUserList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserList(StatementParser.CreateUserListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#defaultRoleClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultRoleClause(StatementParser.DefaultRoleClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#requireClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRequireClause(StatementParser.RequireClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#connectOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectOptions(StatementParser.ConnectOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#accountLockPasswordExpireOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccountLockPasswordExpireOptions(StatementParser.AccountLockPasswordExpireOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#accountLockPasswordExpireOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccountLockPasswordExpireOption(StatementParser.AccountLockPasswordExpireOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUser(StatementParser.AlterUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterUserEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUserEntry(StatementParser.AlterUserEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#alterUserList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterUserList(StatementParser.AlterUserListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropUser(StatementParser.DropUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#createRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRole(StatementParser.CreateRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#dropRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropRole(StatementParser.DropRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#renameUser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenameUser(StatementParser.RenameUserContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setDefaultRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetDefaultRole(StatementParser.SetDefaultRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetRole(StatementParser.SetRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#setPassword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetPassword(StatementParser.SetPasswordContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#authOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuthOption(StatementParser.AuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#withGrantOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithGrantOption(StatementParser.WithGrantOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userOrRoles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserOrRoles(StatementParser.UserOrRolesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#roles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoles(StatementParser.RolesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#grantAs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrantAs(StatementParser.GrantAsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#withRoles}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithRoles(StatementParser.WithRolesContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserAuthOption(StatementParser.UserAuthOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifiedBy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifiedBy(StatementParser.IdentifiedByContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#identifiedWith}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifiedWith(StatementParser.IdentifiedWithContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#connectOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectOption(StatementParser.ConnectOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#tlsOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTlsOption(StatementParser.TlsOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link StatementParser#userFuncAuthOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserFuncAuthOption(StatementParser.UserFuncAuthOptionContext ctx);
}