% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/zgcamusa_xml_elc_emissions.R
\name{module_gcamusa_elc_emissions_xml}
\alias{module_gcamusa_elc_emissions_xml}
\title{module_gcamusa_elc_emissions_xml}
\usage{
module_gcamusa_elc_emissions_xml(command, ...)
}
\arguments{
\item{command}{API command to execute}

\item{...}{other optional parameters, depending on command}
}
\value{
Depends on \code{command}: either a vector of required inputs,
a vector of output names, or (if \code{command} is "MAKE") all
the generated outputs: \code{elc_emissions_USA.xml}. The corresponding file in the
original data system was \code{batch_elc_nonghg_USA.R} (gcamusa XML).
}
\description{
Construct XML data structure for \code{elc_emissions_USA.xml}.
}
