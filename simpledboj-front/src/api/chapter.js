import request from '@/utils/request'
import axios from 'axios'
import { getToken } from '@/utils/auth'

export function chapterTree() {
  return request({
    url: '/module/chapterTree',
    method: 'get'
  })
}

export function getDocument(id) {
  return request({
    url: `/module/getDocument/${id}`,
    method: 'get'
  })
}

export function downloadModule(id) {
  return request({
    url: `/module/downloadModule/${id}`,
    method: 'get'
  })
}

export function getModuleFile(id) {
  return request({
    url: `/module/getModuleFile/${id}`,
    method: 'get'
  })
}

export function uploadFileSingle(data) {
  return axios.post(`${process.env.VUE_APP_BASE_API}/file/uploadFile`, data, {
      headers: {
          "Content-Type": "multipart/form-data",
          "Token": getToken(),
        }
  })
}

export function addModule(data) {
  return request({
    url: `/module/addModule`,
    method: 'post',
    data
  })
}

export function deleteChapter(data) {
  return request({
    url: `/module/deleteChapter`,
    method: 'post',
    data
  })
}

export function deleteBatchChapter(data) {
  return request({
    url: `/module/deleteBatchChapter`,
    method: 'post',
    data
  })
}

export function addCase(data) {
  return request({
    url: `/module/test/addCase`,
    method: 'post',
    data
  })
}

export function pageCase(page, limit, data) {
  return request({
    url: `/module/test/pageCase/${page}/${limit}`,
    method: 'post',
    data
  })
}

export function getModule(id) {
  return request({
    url: `/module/getModule/${id}`,
    method: 'get'
  })
}

export function deleteBatchTestProcedure(data) {
  return request({
    url: `/module/test/deleteBatchTestProcedure`,
    method: 'post',
    data
  })
}

export function deleteTestProcedure(data) {
  return request({
    url: `/module/test/deleteTestProcedure`,
    method: 'post',
    data
  })
}

export function startJudge(id) {
  return request({
    url: `/module/judge/${id}`,
    method: 'post',
    data: { moduleId: id }
  })
}
