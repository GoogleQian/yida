<template>
    <div class="dept box">
        <!-- 搜索 -->
        <!--<z-search :searchItems="searchItems" @search="search"></z-search>-->
        <!-- 功能+table -->
        <z-table :tableData="tableData" :tableColumns="tableColumns" :page="page" :funcs="funcs" @func="func" @handleCurrentChange="handleCurrentChange" v-loading="tableLoading"></z-table>
        <!-- 新增/编辑dialog -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
            <el-form :model="dialogData" ref="ruleForm" label-width="100px" class="demo-ruleForm" :disabled="dialogTitle === '详情'">
                <el-form-item label="部门名称：" prop="name">
                    <el-input v-model="dialogData.name"></el-input>
                </el-form-item>
                <el-form-item label="上级部门：" prop="parentId">
                        <p style="width: 200px;height: 30px;line-height: 30px;border: 1px solid #eee;background: #eee;cursor: pointer;" @click="deptVisible = true">{{dialogData.parentName}}</p>
                </el-form-item>
                <el-form-item label="排序号：" prop="orderNum">
                    <el-input-number v-model="dialogData.orderNum" controls-position="right"  :min="0"></el-input-number>
                </el-form-item>
            </el-form>
            <el-dialog title="选择部门" :visible.sync="deptVisible" z-index="600">
                <el-tree
                        :data="allDept"
                        :props="defaultProps"
                        accordion
                        @node-click="handleNodeClick">
                </el-tree>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="deptVisible = false">取 消</el-button>
                    <el-button type="primary" @click="confirmDeptId">确 定</el-button>
                </div>
            </el-dialog>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('ruleForm')" :loading="btnLoading">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import ZTable from "@/components/z-table/z-table";
    import { tableColumns, searchItems } from "./data";
    import { deptList, dept, addDept, editDept, delDept,deptSelect } from "./api";
    import * as util from "@/common/js/util";
    export default {
        data() {
            return {
                selectedDeptId: null,
                selectedDeptName: null,
                tableData: [],
                page: {
                    total: 0,
                    currentPage: 1
                },
                tableColumns,
                searchItems,
                tableLoading: false,
                searchForm: {},
                dialogTitle: "新增",
                dialogVisible: false,
                deptVisible: false,
                labelWidth: "100px",
                dialogData: {
                    schemalist: []
                },
                allDept: [],
                treeLoading: false,
                defaultProps: {
                    children: "children",
                    label: "name"
                },
                btnLoading: false
            };
        },
        created() {
            this.getList();
        },
        methods: {
            getList() {
                this.tableLoading = true;
                const params = { page: this.page.currentPage, ...this.searchForm };
                deptList(params).then(res => {
                    this.tableLoading = false;
                    this.tableData = res.deptlist;
                    // this.page.total = res.page.totalCount;
                });
            },
            search(searchForm) {
                this.page.currentPage = 1;
                this.searchForm = searchForm;
                this.getList();
            },
            func({ opera, row }) {
                switch (opera) {
                    case "查看":
                        this.view(row);
                        break;
                    case "修改":
                        this.edit(row);
                        break;
                    case "新增":
                        this.add();
                        break;
                    case "删除":
                        this.del(row);
                        break;
                }
            },
            add() {
                this.dialogTitle = "新增";
                this.dialogData = {};
                this.dialogVisible = true;
                deptSelect().then(res => {
                    if (res.code === 0) {
                        this.allDept = util.formatterData(res.deptList, "deptId");
                    }
                });
            },
            view(row) {
                this.dialogTitle = "详情";
                this.dialogData = { ...row };
                this.dialogVisible = true;
                dept({ deptId: row.deptId }).then(res => {
                    if (res.code === 0) {
                        this.dialogData = res.dept;
                    }
                });
            },
            edit(row) {
                this.dialogTitle = "编辑";
                this.dialogData = { ...row };
                this.dialogVisible = true;
                dept({ deptId: row.deptId }).then(res => {
                    if (res.code === 0) {
                        this.dialogData = res.dept;
                    }
                });
                deptSelect().then(res => {
                    if (res.code === 0) {
                        this.allDept = util.formatterData(res.deptList, "deptId");
                    }
                });
            },
            del(row) {
                this.$confirm("此操作将永久删除该条数据, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(() => {
                        delDept([row[0].deptId]).then(res => {
                            this.$message({
                                message: res.code === 0 ? "删除成功" : res.msg,
                                type: res.code === 0 ? "success" : "error"
                            });
                            this.getList();
                        });
                    })
                    .catch(() => {
                        this.$message({
                            type: "info",
                            message: "已取消删除"
                        });
                    });
            },
            handleCurrentChange(val) {
                this.page.currentPage = val;
                this.getList();
            },
            submitForm(formName) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.btnLoading = true;
                        if (this.dialogTitle === "编辑") {
                            console.log("324254");
                            editDept({
                                ...this.dialogData,
                            }).then(res => {
                                console.log("111111");
                                this.btnLoading = false;
                                if (res.code == 0) {
                                    this.$message({
                                        message: "修改成功",
                                        type: "success"
                                    });
                                    this.dialogVisible = false;
                                    this.getList();
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "error"
                                    });
                                }
                            });
                        } else if (this.dialogTitle === "新增") {
                            addDept({
                                ...this.dialogData,
                            }).then(res => {
                                if (res.code == 0) {
                                    this.$message({
                                        message: "新增成功",
                                        type: "success"
                                    });
                                    this.dialogVisible = false;
                                    this.getList();
                                } else {
                                    this.$message({
                                        message: res.msg,
                                        type: "error"
                                    });
                                }
                            });
                        }else if(this.dialogTitle === '详情'){
                            this.dialogVisible = false;
                        }
                    } else {
                        return false;
                    }
                });
            },
            handleNodeClick(val) {
                console.log(val);
                this.selectedDeptId = val.deptId;
                this.selectedDeptName = val.name
            },
            confirmDeptId() {
                this.deptVisible = false;
                this.dialogData.parentId = this.selectedDeptId;
                this.dialogData.parentName = this.selectedDeptName;
            }
        },
        computed: {
            funcs() {
                return util.funcsParse(this.$route, this.$store.state.auth.menus);
            }
        },
        components: { ZTable}
    };
</script>

<style scoped lang="less">
</style>
