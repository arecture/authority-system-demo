import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
// 导入qs
import qs from 'qs'
// 创建axios异步请求实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 15000 // 请求超时时间
})
// 请求前拦截
service.interceptors.request.use(
  config => {
    // 判断store中是否存在token
    if (store.getters.token) {
      // 读取token并把他存到head头部信息里
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)
// 响应时拦截
service.interceptors.response.use(
  
  response => {
    // 获取后端返回的数据
    const res = response.data
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token
      expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // 重新登陆
        MessageBox.confirm('用户登录信息过期,请重新登陆', '系统提示', {
          confirmButtonText: '登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
//请求方法
const http = {
  // post请求提交
  post(url, params) {
    return service.post(url, params, {
      transformRequest: [(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },
  // rest风格的get请求
  put(url, params) {
    return service.put(url, params, {
      transformRequest: [(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type': 'application/json'
      }
    })
  },
  get(url, params) {
    return service.get(url, {
      params: params,
      paramsSerializer: (params) => {
        return qs.stringify(params)
      }
    })
  },
  getRestApi(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        if (params.hasOwnProperty(key) && params[key] !== null && params[key]
          !== '') {
          _params += `${params[key]}/`
        }
      }
      _params = _params.substr(0, _params.length - 1)
    }
    if (_params) {
      return service.get(`${url}${_params}`)
    } else {
      return service.get(url)
    }
  },
  // 删除请求
  delete(url, params) {
    let _params
    if (Object.is(params, undefined || null)) {
      _params = ''
    } else {
      _params = '/'
      for (const key in params) {
        if (params.hasOwnProperty(key) && params[key] !== null && params[key]
          !== '') {
          _params += `${params[key]}/`
        }
      }
      _params = _params.substr(0, _params.length - 1)
    }
    if (_params) {
      return service.delete(`${url}${_params}`).catch(err => {
        message.error(err.msg)
        return Promise.reject(err)
      })
    } else {
      return service.delete(url).catch(err => {
        message.error(err.msg)
        return Promise.reject(err)
      })
    }
  },
  // 文件上传请求
  upload(url, params) {
    return service.post(url, params, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  // 登录请求
  login(url, params) {
    return service.post(url, params, {
      transformRequest: [(params) => {
        return qs.stringify(params)
      }],
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  }
}
export default http