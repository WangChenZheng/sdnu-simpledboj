<template>
  <div>
    <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
      <div style="flex: 1">
        <el-image style="width: 100%; height: 100%" src="http://www.sdnu.edu.cn/images/header/shuyuan.png" fit="cover" />
        <div class="start-title">SDNU SimpleDB Online Judge</div>
      </div>
      <div style="width: 400px">
        <div style="margin-top: 250px; text-align: center">
          <div style="font-size: 25px; color: black; font-weight: bold">登录</div>
          <div style="font-size: 14px; color: grey; margin-top: 10px">
            欢迎来到SDNU SimpleDB OJ
          </div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
            label-position="left">
            <div style="margin: 30px 40px 0px 40px">
              <el-input type="text" placeholder="学号" v-model="loginForm.username">
                <template #prefix>
                  <el-icon>
                    <User />
                  </el-icon>
                </template>
              </el-input>
              <el-input style="margin-top: 10px" type="password" placeholder="密码" v-model="loginForm.password">
                <template #prefix>
                  <el-icon>
                    <Lock />
                  </el-icon>
                </template>
              </el-input>
            </div>
            <div style="margin: 10px 40px 0px 40px; height: 32px">
              <el-row>
                <el-col :span="12" style="text-align: left">
                  <!-- <el-checkbox label="记住我"></el-checkbox> -->
                </el-col>
                <el-col :span="12" style="text-align: left; line-height: 32px;">
                  <!-- <span style="font-size: 14px; color: grey; cursor: pointer">忘记密码?</span> -->
                </el-col>
              </el-row>
            </div>
            <div style="margin-top: 40px">
              <el-button :loading="loading" style="width: 270px" type="success" plain
                @click.native.prevent="handleLogin">立即登录</el-button>
            </div>
            <el-divider style="width: 80%; margin: 30px auto">
              <span style="color: grey; font-size: 13px">没有账号？</span>
            </el-divider>
            <div>
              <el-button style="width: 270px" type="warning" plain disabled>注册账号</el-button>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '201911010223',
        password: 'wc123456'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.start-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: black;
  text-shadow: 0 0 10px grey;
  font-size: 30px;
}
</style>
