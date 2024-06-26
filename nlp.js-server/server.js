const express = require('express')
const methodOverride = require('method-override')

const swaggerUi = require('swagger-ui-express')
const openApiDocumentation = require('./openapi.json')

const indexRouter = require('./routes/index')
const apiRouter = require('./routes/api')

const server = express()
const port = 6666

server.use(express.json({limit: '100mb'}));
server.use(express.urlencoded({limit: '100mb', extended: false}));
server.use('/', indexRouter)
server.use('/api', apiRouter)
server.use(methodOverride())
server.use(logErrors)
server.use(errorHandler)
server.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openApiDocumentation));

server.listen(port, () => {
    console.log("xatkit-nlp.js-server running on port", port)
})

function logErrors (err, req, res, next) {
    console.error(err.stack)
    next(err)
}
function errorHandler (err, req, res, next) {
    res.status(500).json({ message: err.message })
}
