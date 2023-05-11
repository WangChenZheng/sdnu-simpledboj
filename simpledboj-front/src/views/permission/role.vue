<template>
  <div style="padding: 20px;">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-button type="primary" style="width: 100px;" @click="dialogFormVisible = true">添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-show="multipleSelection.length > 0" type="danger" @click="deleteBatchRole1">删除已选</el-button>
      </el-form-item>
    </el-form>
    <el-dialog title="添加角色" :visible.sync="dialogFormVisible">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="roleName" label="角色名" >
          <el-input v-model="form.roleName" placeholder="例如：管理员"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="角色" >
          <el-input v-model="form.name" placeholder="例如: admin"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole1">确 定</el-button>
      </div>
    </el-dialog>
    <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="ID" width="300">
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名" width="120">
      </el-table-column>
      <el-table-column prop="name" label="角色" width="300" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listRole, deleteRole, deleteBatchRole, addRole } from '@/api/role'
export default {
  data() {
    return {
      dialogFormVisible: false,
      form: {
        roleName: '',
        name: ''
      },
      tableData: [],
      multipleSelection: [],
      rules: {
        roleName: [
          { required: true, message: '请输入角色名', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入角色', trigger: 'blur' },
        ]
      }
    }
  },
  created() {
    this.listRole1()
  },
  methods: {
    addRole1() {
      addRole(this.form).then(data => {
        this.$message({
          message: data.message,
          type: 'success'
        });
        this.dialogFormVisible = false
        this.form = { roleName: '', name: '' }
        this.listRole1()
      })
    },
    deleteBatchRole1() {
      this.$confirm('此操作将永久删除角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchRole(this.multipleSelection).then(data => {
          this.$message(data.message);
          this.listRole1()
        })
      })

    },
    handleEdit(index, data) {
      this.dialogFormVisible = true
      this.form = data
    },
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(data).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.listRole1()
        })
      })
    },
    listRole1() {
      listRole().then(data => {
        this.tableData = data.data.list
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>
