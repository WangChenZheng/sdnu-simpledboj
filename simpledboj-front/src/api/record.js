import request from '@/utils/request'

export function getRecordList(id) {
  return request({
    url: `/judger/record/getRecordList`,
    method: 'get'
  })
}
