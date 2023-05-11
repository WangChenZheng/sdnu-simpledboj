import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/loginByPwd',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/auth/getUserInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

export function pageList(page, offset, data) {
  return request({
    url: `/ucenter/page/${page}/${offset}`,
    method: 'post',
    data
  })
}

export function addUser(data) {
  return request({
    url: `/ucenter/addUser`,
    method: 'post',
    data
  })
}

export function addBatchUser(data) {
  return request({
    url: `/ucenter/addBatchUser`,
    method: 'post',
    data
  })
}

export function deleteBatchUser(data) {
  return request({
    url: `/ucenter/deleteBatchUser`,
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: `/ucenter/deleteUser`,
    method: 'post',
    data
  })
}
