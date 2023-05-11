<template>
  <div style="padding: 20px;">
    <el-form :inline="true" :model="query" class="demo-form-inline">
      <el-form-item label="学号/工号">
        <el-input v-model="query.username" placeholder="学号/工号"></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="query.name" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" >
        <el-input v-model="query.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="pageList1">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="clearQuery">清空</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="goto('/ucenter/addUser')">添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="goto('/ucenter/addBatchUser')">批量添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-show="multipleSelection.length > 0" type="danger" @click="deleteBatchUser1">删除已选用户</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="学号/工号" width="120">
        <template slot-scope="scope">{{ scope.row.username }}</template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="120">
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="300" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="tag" label="标签" width="200"
        :filters="filters" :filter-method="filterTag"
        filter-placement="bottom-end" >
        <template slot-scope="scope">
          <el-tag v-for="(role, index) in scope.row.roles" :key="index" :type="scope.row.tag === '管理员' ? 'primary' : 'success'" disable-transitions>{{ role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination @current-change="getPageList" :current-page="page" :page-size="limit" layout="total, prev, pager, next, jumper" :total="total"
        prev-text="上一页" next-text="下一页" hide-on-single-page>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { pageList, deleteBatchUser, deleteUser } from '@/api/user'
import { listRole } from '@/api/role'
export default {
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      query: {
        name: '',
        username: '',
        email: ''
      },
      tableData: [],
      multipleSelection: [],
      filters: [],
    }
  },
  created() {
    this.pageList1()
    listRole().then(data => {
      this.filters = []
      data.data.list.forEach(elem => {
        this.filters.push({
          text: elem.roleName,
          value: elem.roleName
        })
      })
    })
  },
  methods: {
    deleteBatchUser1() {
      this.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBatchUser(this.multipleSelection).then(data => {
          this.$message(data.message);
          this.pageList1()
        })
      })

    },
    handleEdit(index, data) {
      this.$router.push({
        path: '/ucenter/addUser',
        query: {
          user: data
        }
      })
    },
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(data).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.pageList1()
        })
      })
    },
    goto(url) {
      this.$router.push(url)
    },
    clearQuery() {
      this.query.name = ''
      this.query.username = ''
      this.query.email = ''
      this.pageList1()
    },
    pageList1() {
      pageList(this.page, this.limit, this.query).then(data => {
        this.tableData = data.data.list
        this.total = data.data.total
      })
    },
    getPageList(e) {
      this.page = e
      this.pageList1()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    filterTag(value, row) {
      return row.roles.includes(value);
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    }
  }
}
</script>
