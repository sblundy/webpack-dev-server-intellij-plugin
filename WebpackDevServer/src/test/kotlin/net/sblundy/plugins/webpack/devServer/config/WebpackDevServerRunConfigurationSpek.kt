package net.sblundy.plugins.webpack.devServer.config

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef
import com.intellij.openapi.command.impl.DummyProject
import com.intellij.openapi.project.Project
import com.winterbe.expekt.expect
import org.jdom.Element
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

class WebpackDevServerRunConfigurationSpek: Spek({
    describe("WebpackDevServerRunConfiguration") {
        val type = WebPackDevServerConfigType()

        val factory = type.defaultFactory
        val dummyProject: Project = DummyProject.getInstance()

        given("an empty config from the template") {
            val template = factory.createTemplateConfiguration(dummyProject) as WebpackDevServerRunConfiguration
            on("round trip writeExternal -> readExternal") {
                val config = Element("config")
                val target = WebpackDevServerRunConfiguration(dummyProject, factory, "test")

                template.writeExternal(config)
                target.readExternal(config)

                itsFieldsShouldMatch(this, template, target)
            }
        }

        given("a fully populated source") {
            val source = WebpackDevServerRunConfiguration(dummyProject, factory, "source")
            source.interpreterRef = NodeJsInterpreterRef.create("test-ref")
            source.basePath = "/dev/null"
            source.nodeModulesDir = "/project/node_modules"
            source.webPackConfigFile = "/project/webpack.config.json"
            source.nodeOptions = "-T"
            source.portNumber = "1234"
            source.workingDir = "/project"

            on("round trip writeExternal -> readExternal") {
                val config = Element("config")
                val target = WebpackDevServerRunConfiguration(dummyProject, factory, "target")

                source.writeExternal(config)
                target.readExternal(config)

                itsFieldsShouldMatch(this, source, target)
            }
        }
    }
})

fun itsFieldsShouldMatch(dsl: Dsl, expected: WebpackDevServerRunConfiguration, actual: WebpackDevServerRunConfiguration): Unit {
    dsl.it("should have same interpreterRef") {
        expect(actual.interpreterRef.referenceName).to.equal(expected.interpreterRef.referenceName)
    }
    dsl.it("should have same portNumber") {
        expect(actual.portNumber).to.equal(expected.portNumber)
    }
    dsl.it("should have same nodeOptions") {
        expect(actual.nodeOptions).to.equal(expected.nodeOptions)
    }
    dsl.it("should have same webPackConfigFile") {
        expect(actual.webPackConfigFile).to.equal(expected.webPackConfigFile)
    }
    dsl.it("should have same nodeModulesDir") {
        expect(actual.nodeModulesDir).to.equal(expected.nodeModulesDir)
    }
    dsl.it("should have same workingDir") {
        expect(actual.workingDir).to.equal(expected.workingDir)
    }
    dsl.it("should have same baseDir") {
        expect(actual.basePath).to.equal(expected.basePath)
    }
}