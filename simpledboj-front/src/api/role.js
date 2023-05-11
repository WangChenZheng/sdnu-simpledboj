import request from '@/utils/request'


export function listRole() {
  return request({
    url: '/ucenter/role/listRole',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/ucenter/role/addRole',
    method: 'post',
    data
  })
}

export function deleteRole(data) {
  return request({
    url: '/ucenter/role/deleteRole',
    method: 'post',
    data
  })
}

export function deleteBatchRole(data) {
  return request({
    url: '/ucenter/role/deleteBatchRole',
    method: 'post',
    data
  })
}
