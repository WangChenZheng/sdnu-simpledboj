<template>
  <div style="padding: 20px;">
    <el-form :inline="true" :model="query" class="demo-form-inline">
      <el-form-item label="章节绑定" prop="moduleId">
        <el-cascader :props="{ value: 'id', checkStrictly: true }"
          :options="options" @change="handleChange"></el-cascader>
      </el-form-item>
      <el-form-item style="margin-left: 25px;">
        <el-button type="primary" style="width: 80px;" @click="getPageCase">查询</el-button>
      </el-form-item>
      <el-form-item style="margin-left: 25px;">
        <el-button type="primary" style="width: 80px;" @click="goto('/course/addCase')">添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-show="multipleSelection.length > 0" type="danger" @click="deleteBatchUser1">删除已选记录</el-button>
      </el-form-item>
    </el-form>
    <el-table border ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange" row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column type="selection" width="55" />
      <el-table-column label="文件名" sortable width="200">
        <template slot-scope="scope">{{ scope.row.filename }}</template>
      </el-table-column>
      <el-table-column prop="moduleName" label="对应章节" width="120" show-overflow-tooltip />
      <el-table-column prop="path" label="文件路径" width="200" show-overflow-tooltip />
      <el-table-column prop="userFilePath" label="测试程序生成文件路径" width="200" show-overflow-tooltip />
      <el-table-column prop="resultPath" label="正确结果文件存放路径" width="200" show-overflow-tooltip />
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
import { chapterTree, pageCase, deleteBatchTestProcedure, deleteTestProcedure } from "@/api/chapter";
export default {
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      query: {
        moduleId: ''
      },
      options: [],
      tableData: [],
      multipleSelection: []
    }
  },
  mounted() {
    if (this.$route.query.moduleId) {
      this.query.moduleId = this.$route.query.moduleId
    }
    this.getChapterTree()
    this.getPageCase()
  },
  methods: {
    getPageCase() {
      pageCase(this.page, this.limit, {
        moduleId: this.query.moduleId
      }).then(data => {
        this.tableData = data.data.list
        this.total = data.data.total
      })
    },
    addCase(index, data) {
      this.$router.push({
        path: '/course/addCase'
      })
    },
    handleEdit(index, data) {
      this.$router.push({
        path: '/course/addCase',
        query: {
          case: data
        }
      })
    },
    getChapterTree() {
      chapterTree().then((data) => {
        this.options = data.data.list;
      });
    },
    deleteBatchUser1() {
      this.$confirm('此操作将永久删除章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchTestProcedure(this.multipleSelection).then(data => {
          this.$message(data.message);
          this.getPageCase()
        })
      })

    },
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTestProcedure(data).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.getPageCase()
        })
      })
    },
    goto(url) {
      this.$router.push(url)
    },
    handleChange(value) {
      this.query.moduleId = value[value.length - 1]
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>
