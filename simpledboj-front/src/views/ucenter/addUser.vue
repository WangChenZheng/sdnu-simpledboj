<template>
  <div style="padding: 20px;">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="margin-top: 20px;">
      <el-form-item label="学号/工号" prop="username">
        <el-input v-model="form.username" style="width: 240px;" placeholder="请输入学号/工号"></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" style="width: 180px;" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="班级" prop="clazz">
        <el-input v-model="form.clazz" style="width: 180px;" placeholder="请输入班级"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" style="width: 280px;" placeholder="可为空"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="roles" >
        <el-select v-model="form.roles" multiple placeholder="请选择角色" style="width: 260px;">
          <el-option v-for="item in roleOptions" :key="item.id" :label="item.roleName" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" style="width: 220px;" placeholder="可为空，默认为学号后六位"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">完成</el-button>
        <el-button @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addUser } from '@/api/user'
import { listRole } from '@/api/role'
export default {
  data() {
    return {
      roleOptions: [],
      form: {
        name: '',
        username: '',
        email: '',
        clazz: '',
        password: '',
        roles: []
      },
      rules: {
        username: [
          { required: true, message: '请输入正确的学号/工号', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' },
        ],
        clazz: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ],
        roles: [
          { required: true, message: '请输入选择角色', trigger: 'blur' }
        ],
      }
    }
  },
  mounted() {
    if (this.$route.query.user) {
      // this.form.id = this.$route.query.user.name
      // this.form.name = this.$route.query.user.name
      // this.form.username = this.$route.query.user.username
      // this.form.email = this.$route.query.user.email
      // this.form.clazz = this.$route.query.user.clazz
      // this.form.password = this.$route.query.user.password
      this.form = this.$route.query.user
    }
    listRole().then(data => {
      this.roleOptions = data.data.list
    })
  },
  methods: {
    onSubmit() {
      let roles = []
      this.form.roles.forEach(elem => {
        this.roleOptions.forEach(elem1 => {
          if (elem == elem1.roleName){
            roles.push(elem1.id)
          }
          if (elem == elem1.id) {
            roles.push(elem1.id)
          }
        })
      })
      let form = JSON.parse(JSON.stringify(this.form))
      form.roles = roles
      addUser(form).then(data => {
        this.$message({
          message: data.message,
          type: 'success'
        });
      })
    },
    clear() {
      this.form.username = ''
      this.form.name = ''
      this.form.email = ''
      this.form.clazz = ''
      this.form.password = ''
    }
  }
}
</script>
