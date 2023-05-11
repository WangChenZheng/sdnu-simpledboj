<template>
  <div style="padding: 20px;">
    <el-form :inline="true" :model="query" class="demo-form-inline">
      <el-form-item style="margin-left: 25px;">
        <el-button type="primary" style="width: 80px;" @click="goto('/course/addCourse')">添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-show="multipleSelection.length > 0" type="danger" @click="deleteBatchUser1">删除已选章节</el-button>
      </el-form-item>
    </el-form>
    <el-table border ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange" row-key="id" :tree-props="{ children: 'children' }">
      <el-table-column type="selection" width="55" />
      <el-table-column label="章节名" sortable width="120">
        <template slot-scope="scope">{{ scope.row.label }}</template>
      </el-table-column>
      <el-table-column prop="details" label="描述" width="200" show-overflow-tooltip />
      <el-table-column prop="modulePath" label="模块路径" width="200" show-overflow-tooltip />
      <el-table-column prop="caseNum" label="测试案例数" width="120" sortable />
      <el-table-column prop="submitNum" label="提交人数" width="120" sortable />
      <el-table-column prop="acceptNum" label="成功人数" width="120" sortable />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" @click="addCase(scope.$index, scope.row)">添加测试案例</el-button>
          <el-button size="mini" @click="gotoCaseList(scope.$index, scope.row)">查看测试案例</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { chapterTree, getModuleFile, deleteChapter, deleteBatchChapter } from "@/api/chapter";
export default {
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      query: {
        module_name: ''
      },
      tableData: [],
      multipleSelection: []
    }
  },
  created() {
    this.getChapterTree()
  },
  methods: {
    gotoCaseList(index, data) {
      this.$router.push({
        path: '/course/caseList',
        query: {
          moduleId: data.id
        }
      })
    },
    addCase(index, data) {
      this.$router.push({
        path: '/course/addCase',
        query: {
          module: data
        }
      })
    },
    handleEdit(index, data) {
      this.$router.push({
        path: '/course/addCourse',
        query: {
          module: data
        }
      })
    },
    getChapterTree() {
      chapterTree().then((data) => {
        this.tableData = data.data.list;
      });
    },
    deleteBatchUser1() {
      this.$confirm('此操作将永久删除章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchChapter(this.multipleSelection).then(data => {
          this.$message(data.message);
          this.getChapterTree()
        })
      })

    },
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除章节, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteChapter(data).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.getChapterTree()
        })
      })
    },
    goto(url) {
      this.$router.push(url)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
</script>
